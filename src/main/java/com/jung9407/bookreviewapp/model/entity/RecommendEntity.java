package com.jung9407.bookreviewapp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "recommend")
public class RecommendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private GeneralForumEntity generalForum;

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

    public static RecommendEntity getRecommendEntity(MemberEntity memberEntity, GeneralForumEntity generalForumEntity) {
        RecommendEntity entity = new RecommendEntity();
        entity.setMember(memberEntity);
        entity.setGeneralForum(generalForumEntity);
        return entity;
    }
}
