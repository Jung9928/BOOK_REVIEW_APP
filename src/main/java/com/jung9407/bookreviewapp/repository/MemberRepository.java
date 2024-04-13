package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    MemberEntity findByEmailAndPassword(String email, String password);

    Optional<MemberEntity> findByMemberId(String memberId);

    Optional<MemberEntity> findEmailByMemberId(String memberId);

    int deleteByMemberId(String memberId);
}
