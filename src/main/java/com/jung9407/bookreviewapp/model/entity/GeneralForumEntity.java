package com.jung9407.bookreviewapp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "general_forum")
public class GeneralForumEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "post_id")
   private Long postId;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "member_id")
   private MemberEntity member;

   @Size(min = 2, max=40, message = "제목은 2자이상 40자 이하입니다.")
   @Column(name = "title", nullable = false)
   private String title;

   @Lob
   @Column(name = "content", columnDefinition = "BLOB")
   private byte[] content;

   @Column(name = "vw_cnt")
   private int viewCount;

   @Column(name = "rcmnd_cnt")
   private int recommendCount;

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

   public static GeneralForumEntity getPostEntity(String title, byte[] content, MemberEntity memberEntity) {
      GeneralForumEntity entity = new GeneralForumEntity();
      entity.setTitle(title);
      entity.setContent(content);
      entity.setMember(memberEntity);
      return entity;
   }
}
