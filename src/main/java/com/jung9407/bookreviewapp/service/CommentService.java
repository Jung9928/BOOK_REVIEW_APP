package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.exception.ApplicationException;
import com.jung9407.bookreviewapp.exception.ErrorCode;
import com.jung9407.bookreviewapp.model.dto.CommentDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.CommentRequestDTO;
import com.jung9407.bookreviewapp.model.entity.CommentEntity;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.repository.CommentRepository;
import com.jung9407.bookreviewapp.repository.GeneralForumRepository;
import com.jung9407.bookreviewapp.repository.MemberRepository;
import com.jung9407.bookreviewapp.util.CommentDeleteStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final MemberRepository memberRepository;

    private final GeneralForumRepository generalForumRepository;

    private final CommentRepository commentRepository;

    // 부모 댓글 & 작성일자 내림차순
    @Transactional
    public List<CommentDTO> findCommentsByGeneralForum(Long postId) {
        generalForumRepository.findById(postId);
        return convertNestedStructure(commentRepository.findCommentByGeneralForum(postId));
    }

    @Transactional
    public Page<CommentDTO> pageList(GeneralForumEntity generalForumEntity, Pageable pageable) {
        Page<CommentEntity> commentEntities;

        commentEntities = commentRepository.findCommentEntityByGeneralForum(generalForumEntity, pageable);
        return commentEntities.map(commentEntity -> CommentDTO.entityToCommentDTO(commentEntity));
    }

    @Transactional
    public CommentDTO createComment(CommentRequestDTO commentRequestDTO) {

        log.info("postId : " + commentRequestDTO.getPostId());

        // 1. 게시글이 존재하는 지 검증
        GeneralForumEntity generalForumEntity = generalForumRepository.findById(commentRequestDTO.getPostId()).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s번 게시글을 찾을 수 없습니다.", commentRequestDTO.getPostId())));

//        // 2. 회원 존재 여부 검증
//        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
//                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        //
        CommentEntity commentEntity = commentRepository.save(
                CommentEntity.getCommentEntity(
                        memberRepository.findByMemberId(commentRequestDTO.getMemberId()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", commentRequestDTO.getMemberId()))),
                        generalForumRepository.findById(commentRequestDTO.getPostId()).orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s번 게시글을 찾을 수 없습니다.", commentRequestDTO.getPostId()))),
                        commentRequestDTO.getParentId() != null ? commentRepository.findById(commentRequestDTO.getParentId()).orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, String.format("%s번 댓글을 찾을 수 없습니다.", commentRequestDTO.getParentId()))) : null,
                        commentRequestDTO.getContent()
        ));

        return CommentDTO.entityToCommentDTO(commentEntity);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        CommentEntity commentEntity = commentRepository.findCommentEntityByCommentIdWithParentComment(commentId);

        if(commentEntity.getChildComments().size() != 0) {
            commentEntity.changeCommentDeleteStatus(CommentDeleteStatus.Y);
        }
        else {
            commentRepository.delete(getDeletableAncestorComment(commentEntity));
        }
    }

    private CommentEntity getDeletableAncestorComment(CommentEntity commentEntity) {
        CommentEntity parentComment = commentEntity.getParentComment();

        if(parentComment != null && parentComment.getChildComments().size() == 1 && parentComment.getIsDeleted() == CommentDeleteStatus.Y)
            return getDeletableAncestorComment(parentComment);

        return commentEntity;
    }

    private List<CommentDTO> convertNestedStructure(List<CommentEntity> commentEntities) {
        List<CommentDTO> result = new ArrayList<>();

        Map<Long, CommentDTO> map = new HashMap<>();
        commentEntities.stream().forEach(c -> {
            CommentDTO commentDTO = CommentDTO.entityToCommentDTO(c);
            map.put(commentDTO.getId(), commentDTO);

            if(c.getParentComment() != null)
                map.get(c.getParentComment().getCommentId()).getChildComments().add(commentDTO);
            else
                result.add(commentDTO);
        });



        return result;
    }

    @Transactional
    public CommentDTO updateComment(CommentDTO commentDTO) {
        CommentEntity commentEntity = commentRepository.findById(commentDTO.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.BAD_REQUEST, "%s번 댓글을 찾을 수 없습니다."));
        commentEntity.setComment(commentDTO.getComment());
        commentRepository.updateComment(commentEntity);

        return CommentDTO.entityToCommentDTO(commentEntity);
    }
}
