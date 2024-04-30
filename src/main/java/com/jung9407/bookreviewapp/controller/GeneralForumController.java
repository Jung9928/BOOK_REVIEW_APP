package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.PostCreateDTO;
import com.jung9407.bookreviewapp.model.dto.jwt.CustomMemberDetails;
import com.jung9407.bookreviewapp.model.dto.requestDTO.GeneralForumSearchConditionDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.PostModifyRequestDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.GeneralForumPagingResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.GeneralForumResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.PostModifyResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ResponseResultCode;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.repository.GeneralForumRepository;
import com.jung9407.bookreviewapp.service.GeneralForumService;
import com.jung9407.bookreviewapp.util.PostValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class GeneralForumController {

    private final GeneralForumService generalForumService;

    private GeneralForumRepository generalForumRepository;

    private PostValidator postValidator;

    // 게시글 작성
    @PostMapping("/postCreate")
    public ResponseResultCode<Void> create(@RequestBody PostCreateDTO postCreateDTO, Authentication authentication) {
        generalForumService.create(postCreateDTO.getTitle(), postCreateDTO.getContent(), authentication.getName());
        return ResponseResultCode.success();
    }


    // 게시글 수정
    @PutMapping("/generalForum/{postId}")
    public ResponseResultCode<PostModifyResponseDTO> modify(@PathVariable Long postId, @RequestBody PostModifyRequestDTO postModifyRequestDTO, Authentication authentication) {
        PostModifyResponseDTO postModifyResponseDTO = generalForumService.modify(postModifyRequestDTO.getTitle(), postModifyRequestDTO.getContent(), authentication.getName(), postId);
        System.out.println("========================================");
        System.out.println("postId : " + postModifyResponseDTO.getPostId());
        System.out.println("title : " + postModifyResponseDTO.getTitle());
        System.out.println("content : " + postModifyResponseDTO.getContent().toString());
        System.out.println("memberId : " + postModifyResponseDTO.getMember().getMemberId());
        System.out.println("========================================");

        return ResponseResultCode.success(postModifyResponseDTO);
    }

    // 게시글 삭제
    @DeleteMapping("/generalForum/{postId}")
    public ResponseResultCode<Void> delete(@PathVariable Long postId, Authentication authentication) {
        generalForumService.delete(authentication.getName(), postId);
        return ResponseResultCode.success();
    }


    // 게시글 조회
    @GetMapping("/generalForumList")
    public GeneralForumPagingResponseDTO<List<GeneralForumResponseDTO>> getGeneralForumList(@PageableDefault(sort = {"registeredAt"}) Pageable pageable, GeneralForumSearchConditionDTO generalForumSearchConditionDTO) {
        log.info("searchCategory : " + generalForumSearchConditionDTO.getSearchCategory());
        log.info("searchValue : " + generalForumSearchConditionDTO.getSearchValue());

        return generalForumService.getForumBoardList(pageable, generalForumSearchConditionDTO);
    }

    // 게시글 단건 조회
    @GetMapping("/generalForum/{postId}")
    public ResponseResultCode<GeneralForumResponseDTO> getGeneralForumData(@PathVariable Long postId) {
        log.info("postId : " + postId);

        GeneralForumResponseDTO generalForumResponseDTO = generalForumService.getForumPostData(postId);

        return ResponseResultCode.success(generalForumResponseDTO);
    }

    // 게시글 추천 처리
    @PostMapping("/generalForum/{postId}/recommend")
    public ResponseResultCode<Void> countRecommend(@PathVariable Long postId, Authentication authentication) {

        log.info("postId : " + postId);
        log.info("memberId : " + authentication.getName());
        generalForumService.countRecommend(postId, authentication.getName());

        return ResponseResultCode.success();
    }

    // 게시글 추천 수 가져오기
    @GetMapping("/generalForum/{postId}/recommend")
    public ResponseResultCode<Long> getRecommendCounting(@PathVariable Long postId) {

        Long recommendCount = generalForumService.getRecommendCounting(postId);

        return ResponseResultCode.success(recommendCount);
    }

    // 게시글 조회 수 카운팅(중복 방지 적용)
    @GetMapping("/generalForum/viewCount/{postId}")
    public void getViewCount(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response) {

//        Long viewCount = generalForumService.getViewCounting(postId, request, response);
        generalForumService.getViewCounting(postId, request, response);
    }

    // 내가 작성한 게시글 가져오기
    @GetMapping("/generalForum/my-post-list")
    public ResponseEntity<Page<GeneralForumResponseDTO>> getMyPostList(@PageableDefault(sort = {"registeredAt"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable, @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {

        log.info("id : " + customMemberDetails.getMemberEntity().getId());
        log.info("memberId : " + customMemberDetails.getMemberEntity().getMemberId());
        Page<GeneralForumResponseDTO> generalForumResponseDTOS = generalForumService.getMyPostList(customMemberDetails.getMemberEntity().getId(), pageable);

        return ResponseEntity.ok().body(generalForumResponseDTOS);
    }
}
