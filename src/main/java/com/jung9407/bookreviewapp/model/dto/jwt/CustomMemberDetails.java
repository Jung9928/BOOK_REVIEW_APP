package com.jung9407.bookreviewapp.model.dto.jwt;

import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class CustomMemberDetails extends User {

    private final MemberEntity memberEntity;

    public CustomMemberDetails(MemberEntity memberEntity) {
        super(memberEntity.getMemberId(), memberEntity.getPassword(), List.of(new SimpleGrantedAuthority("ADMIN")));
        this.memberEntity = memberEntity;
    }
}
