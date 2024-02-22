package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.BookReviewAppApplication;
import com.jung9407.bookreviewapp.exception.ApplicationException;
import com.jung9407.bookreviewapp.exception.ErrorCode;
import com.jung9407.bookreviewapp.model.dto.PostCreateDTO;
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
        // postCreateDTO 클래스 -> Entity 변환 필요
//        MemberEntity memberEntity = memberRepository.findByUserId(userId).orElseThrow(() ->
//                new BookReviewAppApplication(ErrorCode.USER_NOT_FOUND, String.format("%s 회원님을 찾을 수 없습니다.", userId)));

        // 작성자 확인
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 가입 후 작성 바랍니다.")));

        // 게시글 저장
        //PostEntity saved = postRepository.save(PostEntity.setPostEntity(postCreateDTO.getTitle(), postCreateDTO.getContent(), memberEntity));
        PostEntity saved = postRepository.save(PostEntity.getPostEntity(title, content, memberEntity));

        // 게시글 리턴

    }
}
