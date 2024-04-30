package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.CommentDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.CommentRequestDTO;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.service.CommentService;
import com.jung9407.bookreviewapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    private final MemberService memberService;

    // 게시글에 (대)댓글 작성
    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentRequestDTO commentRequestDTO) {

//        MemberDTO memberDTO = memberService.getMemberByMemberId(customMemberDetails.getUsername());

        log.info("content : " + commentRequestDTO.getContent());
        log.info("postId : " + commentRequestDTO.getPostId());
        log.info("memberId : " + commentRequestDTO.getMemberId());
        log.info("parentId : " + commentRequestDTO.getParentId());
        log.info("isDeleted : " + commentRequestDTO.getIsDeleted());

        CommentDTO commentDTO = commentService.createComment(commentRequestDTO);
        return ResponseEntity.ok().body(commentDTO);
    }

    // 게시글의 댓글 페이징
    @GetMapping("/list")
    public ResponseEntity<Page<CommentDTO>> commentList(@RequestParam GeneralForumEntity generalForumEntity, @PageableDefault(size=10, sort = "commentId", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("postId : " + generalForumEntity.getPostId());
        log.info("title : " + generalForumEntity.getTitle());
        log.info("memberId : " + generalForumEntity.getMember().getMemberId());
        log.info("content : " + generalForumEntity.getContent());
        log.info("comments : " + generalForumEntity.getComments());


        Page<CommentDTO> commentDTOS = commentService.pageList(generalForumEntity, pageable);

        return ResponseEntity.ok().body(commentDTOS);
    }

    // 게시글 댓글의 대댓글 페이징
    @GetMapping("/reply/{postId}")
    public ResponseEntity<List<CommentDTO>> replyList (@PathVariable Long postId) {
        log.info("postId : " + postId);
        List<CommentDTO> replyDTOS = commentService.findCommentsByGeneralForum(postId);
        return ResponseEntity.ok().body(replyDTOS);
    }

    // 댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<CommentDTO> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 수정
    @PutMapping("/modify")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentCreateRequestDto) {
        CommentDTO commentDTO = commentService.updateComment(commentCreateRequestDto);
        return ResponseEntity.ok().body(commentDTO);
    }
}
