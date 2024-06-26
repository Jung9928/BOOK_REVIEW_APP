package com.jung9407.bookreviewapp.model.entity;

import com.jung9407.bookreviewapp.util.CommentDeleteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private GeneralForumEntity generalForum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Column(nullable = false)
    @Lob
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommentEntity parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<CommentEntity> childComments = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private CommentDeleteStatus isDeleted;

    @Column(name = "reg_at")
    private Timestamp registeredAt;

    @Column(name = "mod_at")
    private Timestamp modifiedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = Timestamp.from(Instant.now());
    }

    public static CommentEntity getCommentEntity(MemberEntity memberEntity, GeneralForumEntity generalForumEntity, CommentEntity parentComment, String comment) {
        CommentEntity entity = new CommentEntity();

        entity.setMember(memberEntity);
        entity.setGeneralForum(generalForumEntity);
        entity.setComment(comment);
        entity.setParentComment(parentComment);
        entity.setIsDeleted(CommentDeleteStatus.N);

        return entity;
    }

    public void changeCommentDeleteStatus(CommentDeleteStatus deleteStatus) {
        this.isDeleted = deleteStatus;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
