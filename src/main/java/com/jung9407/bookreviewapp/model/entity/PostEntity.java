package com.jung9407.bookreviewapp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jung9407.bookreviewapp.util.MemberRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "post")
public class PostEntity {

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

   @Column(name = "content", columnDefinition = "TEXT")
   private String content;

   @Column(name = "registed_at")
   private Timestamp registeredAt;

   @Column(name = "modified_at")
   private Timestamp modifiedAt;

   @PrePersist
   void registeredAt() {
      this.registeredAt = Timestamp.from(Instant.now());
   }

   @PreUpdate
   void modifiedAt() {
      this.modifiedAt = Timestamp.from(Instant.now());
   }

   public static PostEntity getPostEntity(String title, String content, MemberEntity memberEntity) {
      PostEntity entity = new PostEntity();
      entity.setTitle(title);
      entity.setContent(content);
      entity.setMember(memberEntity);
      return entity;
   }
}
