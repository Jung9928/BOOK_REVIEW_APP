package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.model.dto.jwt.CustomMemberDetails;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() -> new UsernameNotFoundException("해당 memberId를 찾을 수 없습니다."));

        return new CustomMemberDetails(memberEntity);
    }
}
