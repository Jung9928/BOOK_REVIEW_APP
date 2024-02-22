package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    MemberEntity findByEmailAndPassword(String email, String password);

    //MemberEntity findByMemberId(String userId);
    Optional<MemberEntity> findByMemberId(String memberId);
}
