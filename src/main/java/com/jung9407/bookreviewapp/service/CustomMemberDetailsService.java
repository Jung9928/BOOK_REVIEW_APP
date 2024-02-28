package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.model.dto.jwt.CustomMemberDetails;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomMemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        MemberEntity memberInfo = memberRepository.findByMemberId(memberId).orElseThrow(() -> new UsernameNotFoundException("해당 memberId를 찾을 수 없습니다."));

//        if (memberInfo != null) {
//
//            // UserDetails에 담아서 return 하면 AuthenticationManager가 검증 수행.
//            return new CustomMemberDetails(memberInfo);
//        }

        return new CustomMemberDetails(memberInfo);
    }
}
