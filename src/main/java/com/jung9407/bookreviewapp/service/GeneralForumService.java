package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.exception.ApplicationException;
import com.jung9407.bookreviewapp.exception.ErrorCode;
import com.jung9407.bookreviewapp.model.dto.GeneralForumPaginationDTO;
import com.jung9407.bookreviewapp.model.dto.jwt.CustomMemberDetails;
import com.jung9407.bookreviewapp.model.dto.requestDTO.GeneralForumSearchConditionDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.GeneralForumPagingResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.GeneralForumResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.PostModifyResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ResponseResultCode;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.model.entity.RecommendEntity;
import com.jung9407.bookreviewapp.repository.GeneralForumRepositoryCustom;
import com.jung9407.bookreviewapp.repository.MemberRepository;
import com.jung9407.bookreviewapp.repository.GeneralForumRepository;
import com.jung9407.bookreviewapp.repository.RecommendEntityRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralForumService {

    private final GeneralForumRepositoryCustom generalForumRepositoryCustom;

    private final GeneralForumRepository generalForumRepository;
    private final MemberRepository memberRepository;

    private final RecommendEntityRepository recommendEntityRepository;


    // 게시글 목록 조회
    public GeneralForumPagingResponseDTO<List<GeneralForumResponseDTO>> getForumBoardList(Pageable pageable, GeneralForumSearchConditionDTO generalForumSearchConditionDTO) {

        List<GeneralForumResponseDTO> generalForumResponseDTOList = new ArrayList<>();

        Page<GeneralForumEntity> generalForumEntities = generalForumRepositoryCustom.findAllBySearchCondition(pageable, generalForumSearchConditionDTO);

        for (GeneralForumEntity generalForumEntity : generalForumEntities) {
            GeneralForumResponseDTO generalForumResponseDTO = GeneralForumResponseDTO.builder()
                    .member_id(generalForumEntity.getMember().getMemberId())
                    .post_id(generalForumEntity.getPostId())
//                    .content(generalForumEntity.getContent())
                    .title(generalForumEntity.getTitle())
                    .vw_cnt(generalForumEntity.getViewCount())
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

    // 게시글 단건 조회
    public GeneralForumResponseDTO getForumPostData(long postId) {

        GeneralForumResponseDTO generalForumResponseDTO = new GeneralForumResponseDTO();

        // 게시글이 존재하는 지
        GeneralForumEntity generalForumEntity = generalForumRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s번 게시글을 찾을 수 없습니다.", postId)));

        log.info("postId : " + generalForumEntity.getPostId());
        log.info("memberId : " + generalForumEntity.getMember().getMemberId());
        log.info("title : " + generalForumEntity.getTitle());
        log.info("content : " + generalForumEntity.getContent());
        log.info("viewCount : " + generalForumEntity.getViewCount());
        log.info("registeredAt : " + generalForumEntity.getRegisteredAt());
        log.info("modifiedAt : " + generalForumEntity.getModifiedAt());

        generalForumResponseDTO.setPost_id(generalForumEntity.getPostId());
        generalForumResponseDTO.setMember_id(generalForumEntity.getMember().getMemberId());
        generalForumResponseDTO.setTitle(generalForumEntity.getTitle());
        generalForumResponseDTO.setContent(new String(generalForumEntity.getContent(), StandardCharsets.UTF_8));
        generalForumResponseDTO.setVw_cnt(generalForumEntity.getViewCount());
        generalForumResponseDTO.setRegisteredAt(generalForumEntity.getRegisteredAt());
        generalForumResponseDTO.setModifiedAt(generalForumEntity.getModifiedAt());

        return generalForumResponseDTO;
    }

    // 게시글 작성
    @Transactional
    public void create(String title, String content, String memberId) {

        // 작성자 존재(가입)여부 확인
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 게시글 저장
        generalForumRepository.save(GeneralForumEntity.getPostEntity(title, content.getBytes(StandardCharsets.UTF_8), memberEntity));
    }

    // 게시글 수정
    @Transactional
    public PostModifyResponseDTO modify(String title, String content, String memberId, Long postId) {

        // 게시글 작성자 존재 여부 확인
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 게시글이 존재하는 지
        GeneralForumEntity generalForumEntity = generalForumRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s게시글을 찾을 수 없습니다.", postId)));

        // 게시글 접근 권한 (게시글을 작성한 사람인지) 체크
        if (generalForumEntity.getMember() != memberEntity) {
            throw new ApplicationException(ErrorCode.INVALID_POST_PERMISSION, String.format("%s님은 해당 게시글의 작성자가 아니므로 수정할 수 없습니다.", memberId));
        }

        generalForumEntity.setTitle(title);
        generalForumEntity.setContent(content.getBytes(StandardCharsets.UTF_8));

        return PostModifyResponseDTO.getPostModifyResponseDTO(generalForumRepository.save(generalForumEntity));
    }

    // 게시글 삭제
    @Transactional
    public void delete(String memberId, long postId) {

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 게시글이 존재하는 지
        GeneralForumEntity generalForumEntity = generalForumRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s번 게시글을 찾을 수 없습니다.", postId)));

        // 게시글 접근 권한 (게시글을 작성한 사람인지) 체크
        if (generalForumEntity.getMember() != memberEntity) {
            throw new ApplicationException(ErrorCode.INVALID_POST_PERMISSION, String.format("%s님은 해당 게시글의 작성자가 아니므로 삭제할 수 없습니다.", memberId));
        }

        generalForumRepository.delete(generalForumEntity);
    }

    // 게시글 추천 카운팅
    @Transactional
    public void countRecommend(Long postId, String memberId) {

        // 1. 게시글이 존재하는 지 검증
        GeneralForumEntity generalForumEntity = generalForumRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s번 게시글을 찾을 수 없습니다.", postId)));

        // 2. 회원 존재 여부 검증
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 3. 게시글 추천을 이미 했는지 검증 -> throw
        recommendEntityRepository.findByMemberAndGeneralForum(memberEntity, generalForumEntity).ifPresent(it -> {
            throw new ApplicationException(ErrorCode.ALREADY_RECOMMENDED, String.format("%s님은 %s번 게시글에 대해 이미 추천을 한 상태입니다.", memberId, postId));
        });

        // 4. 게시글 추천 진행
        recommendEntityRepository.save(RecommendEntity.getRecommendEntity(memberEntity, generalForumEntity));
    }


    // 게시글 추천 수 가져오기
    public long getRecommendCounting(Long postId) {

        // 1. 게시글이 존재하는 지 검증
        GeneralForumEntity generalForumEntity = generalForumRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s번 게시글을 찾을 수 없습니다.", postId)));

        // 2. 게시글 추천 수 가져오기
        log.info("postId : " + generalForumEntity.getPostId());

//        List<RecommendEntity> recommendEntities = recommendEntityRepository.findAllByGeneralForum(generalForumEntity);

//        return recommendEntities.size();
        return recommendEntityRepository.countByGeneralForum(generalForumEntity);
    }

    // 내가 작성한 게시글 조회
    public Page<GeneralForumResponseDTO> getMyPostList(long id, Pageable pageable) {
        Page<GeneralForumEntity> generalForumEntities = generalForumRepository.findGeneralForumEntitiesByMemberId(id, pageable);

//        return generalForumEntities.map(generalForumEntity -> GeneralForumResponseDTO.builder().build());
        return generalForumEntities.map(generalForumEntity -> GeneralForumResponseDTO.entityToMemberDTO(generalForumEntity));
    }



    // 게시글 조회 수 증가
    @Transactional
    public void getViewCounting(Long postId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("viewCount")) {
                    oldCookie = cookie;
                }
            }
        }

        // 게시물 조회 수 증가
        if(oldCookie != null) {
            if(!oldCookie.getValue().contains("[" + postId.toString() + "]")) {

                // 기존 쿠기가 있지만 해당 게시글 조회 기록이 없을 때,
                generalForumRepository.countUpView(postId);
                oldCookie.setValue(oldCookie.getValue() + " [" + postId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        }
        else {
            // 기존 쿠기가 없을 때,
            generalForumRepository.countUpView(postId);
            Cookie newCookie = new Cookie("viewCount", "[" + postId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }

//    public void addViewCntToRedis(Long postId) {
//        String key = "generalForumViewCnt::" + postId;
//
//        // 캐시에 값이 없으면 레포지토리에서 조회, 있으면 값을 증가시킴
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//
//        if(valueOperations.get(key) == null) {
//            valueOperations.set(
//                    key,
//                    String.valueOf(generalForumRepository.findGeneralForumViewCountByPostId(postId)),
//            Duration.ofMinutes(5));
//        }
//        else {
//            valueOperations.increment(key);
//        }
//        log.info("viewCount : {}", valueOperations.get(key));
//    }
}
