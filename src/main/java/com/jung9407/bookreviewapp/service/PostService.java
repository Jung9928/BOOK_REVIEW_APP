package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.BookReviewAppApplication;
import com.jung9407.bookreviewapp.exception.ApplicationException;
import com.jung9407.bookreviewapp.exception.ErrorCode;
import com.jung9407.bookreviewapp.model.dto.PostCreateDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.PostModifyResponseDTO;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.model.entity.PostEntity;
import com.jung9407.bookreviewapp.repository.MemberRepository;
import com.jung9407.bookreviewapp.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 게시글 작성
    @Transactional
    public void create(String title, String content, String memberId) {

        // 작성자 존재(가입)여부 확인
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 게시글 저장
        PostEntity saved = postRepository.save(PostEntity.getPostEntity(title, content, memberEntity));
    }

    @Transactional
    public PostModifyResponseDTO modify(String title, String content, String memberId, Long postId) {

        // 게시글 작성자 존재 여부 확인
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 게시글이 존재하는 지
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s 게시글을 찾을 수 없습니다.", postId)));

        // 게시글 접근 권한 (게시글을 작성한 사람인지) 체크
        if (postEntity.getMember() != memberEntity) {
            throw new ApplicationException(ErrorCode.INVALID_POST_PERMISSION, String.format("%s님은 해당 게시글의 작성자가 아니므로 수정할 수 없습니다.", memberId));
        }

        postEntity.setTitle(title);
        postEntity.setContent(content);

        return PostModifyResponseDTO.getPostModifyResponseDTO(postRepository.save(postEntity));
    }

    @Transactional
    public void delete(String memberId, Long postId) {

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        // 게시글이 존재하는 지
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s게시글을 찾을 수 없습니다.", postId)));

        // 게시글 접근 권한 (게시글을 작성한 사람인지) 체크
        if (postEntity.getMember() != memberEntity) {
            throw new ApplicationException(ErrorCode.INVALID_POST_PERMISSION, String.format("%s님은 해당 게시글의 작성자가 아니므로 삭제할 수 없습니다.", memberId));
        }

        postRepository.delete(postEntity);
    }
}
