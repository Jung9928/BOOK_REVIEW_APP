package com.jung9407.bookreviewapp.entity;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TimeZoneStorage;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post {
   private Integer postId;

   private String userId;

   private String title;

   private String content;

   private String z_reg_usr;

   private Timestamp z_reg_dtm;

   private String z_mod_usr;

   private Timestamp z_mod_dtm;
}
