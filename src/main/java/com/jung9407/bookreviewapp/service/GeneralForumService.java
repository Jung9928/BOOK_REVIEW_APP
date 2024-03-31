package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.exception.ApplicationException;
import com.jung9407.bookreviewapp.exception.ErrorCode;
import com.jung9407.bookreviewapp.model.dto.GeneralForumPaginationDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.GeneralForumSearchConditionDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.GeneralForumPagingResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.GeneralForumResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.PostModifyResponseDTO;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.repository.GeneralForumRepositoryCustom;
import com.jung9407.bookreviewapp.repository.MemberRepository;
import com.jung9407.bookreviewapp.repository.GeneralForumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralForumService {

    private final GeneralForumRepositoryCustom generalForumRepositoryCustom;

    private final GeneralForumRepository generalForumRepository;
    private final MemberRepository memberRepository;

    // 게시글 조회
    public GeneralForumPagingResponseDTO<List<GeneralForumResponseDTO>> getForumBoardList(Pageable pageable, GeneralForumSearchConditionDTO generalForumSearchConditionDTO) {

        List<GeneralForumResponseDTO> generalForumResponseDTOList = new ArrayList<>();

        Page<GeneralForumEntity> generalForumEntities = generalForumRepositoryCustom.findAllBySearchCondition(pageable, generalForumSearchConditionDTO);

        for (GeneralForumEntity generalForumEntity : generalForumEntities) {
            GeneralForumResponseDTO generalForumResponseDTO = GeneralForumResponseDTO.builder()
                    .member_id(generalForumEntity.getMember().getMemberId())
                    .post_id(generalForumEntity.getPostId())
                    .content(generalForumEntity.getContent())
                    .title(generalForumEntity.getTitle())
                    .registeredAt(generalForumEntity.getRegisteredAt())
                    .modifiedAt(generalForumEntity.getModifiedAt())
                    .build();

            generalForumResponseDTOList.add(generalForumResponseDTO);
        }

        GeneralForumPaginationDTO generalForumPaginationDTO = new GeneralForumPaginationDTO(
                (int)generalForumEntities.getTotalElements()
                , pageable.getPageNumber() + 1
                , pageable.getPageSize()
                , 10
        );

        return GeneralForumPagingResponseDTO.OK(generalForumResponseDTOList, generalForumPaginationDTO);
    }

    // 게시글 작성
    @Transactional
    public void create(String title, String content, String memberId) {

        // 작성자 존재(가입)여부 확인
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 게시글 저장
        GeneralForumEntity saved = generalForumRepository.save(GeneralForumEntity.getPostEntity(title, content, memberEntity));
    }

    // 게시글 수정
    @Transactional
    public PostModifyResponseDTO modify(String title, String content, String memberId, Long postId) {

        // 게시글 작성자 존재 여부 확인
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 게시글이 존재하는 지
        GeneralForumEntity generalForumEntity = generalForumRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s 게시글을 찾을 수 없습니다.", postId)));

        // 게시글 접근 권한 (게시글을 작성한 사람인지) 체크
        if (generalForumEntity.getMember() != memberEntity) {
            throw new ApplicationException(ErrorCode.INVALID_POST_PERMISSION, String.format("%s님은 해당 게시글의 작성자가 아니므로 수정할 수 없습니다.", memberId));
        }

        generalForumEntity.setTitle(title);
        generalForumEntity.setContent(content);

        return PostModifyResponseDTO.getPostModifyResponseDTO(generalForumRepository.save(generalForumEntity));
    }

    // 게시글 삭제
    @Transactional
    public void delete(String memberId, Long postId) {

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 게시글이 존재하는 지
        GeneralForumEntity generalForumEntity = generalForumRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s게시글을 찾을 수 없습니다.", postId)));

        // 게시글 접근 권한 (게시글을 작성한 사람인지) 체크
        if (generalForumEntity.getMember() != memberEntity) {
            throw new ApplicationException(ErrorCode.INVALID_POST_PERMISSION, String.format("%s님은 해당 게시글의 작성자가 아니므로 삭제할 수 없습니다.", memberId));
        }

        generalForumRepository.delete(generalForumEntity);
    }
}
