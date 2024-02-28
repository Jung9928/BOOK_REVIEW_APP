package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.exception.ApplicationException;
import com.jung9407.bookreviewapp.exception.ErrorCode;
import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberLoginRequestDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberSignupRequestDTO;
import com.jung9407.bookreviewapp.model.dto.jwt.TokenResponseDTO;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.repository.MemberRepository;
import com.jung9407.bookreviewapp.util.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private final BCryptPasswordEncoder encoder;

    // ====================================================
    // 추후, 변경 용이를 위해 yml 또는 properties 파일에서
    // jwt 값을 @Value를 사용하여 yml파일로부터 가져오도록 구현
    // ====================================================
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;


//    public void signup(MemberJoinDTO memberJoinDTO) {
//        // 1. dto -> entity로 변환
//        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberJoinDTO);
//
//        // 2. repository의 save 메소드 호출 (조건, entity객체를 넘겨줘야 함)
//        memberRepository.save(memberEntity);
//    }

    public MemberDTO getMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).map(MemberDTO::entityToMemberDTO).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s는 존재하지 않는 ID 입니다.", memberId)));
    }


    // 회원가입
    @Transactional
    public MemberDTO signup(MemberSignupRequestDTO memberSignupRequestDTO) {
        // 1. 회원가입하려는 memberId가 이미 존재하는 지
        memberRepository.findByMemberId(memberSignupRequestDTO.getMemberId()).ifPresent(it -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_MEMBER_ID, String.format("%s는 이미 존재하는 아이디 입니다.", memberSignupRequestDTO.getMemberId()));
        });

        // 2. 회원가입 진행 = member를 등록
        MemberEntity memberEntity = memberRepository.save(MemberEntity.createMemberEntity(
                memberSignupRequestDTO.getMemberId(),
                encoder.encode(memberSignupRequestDTO.getPassword()),           // 회원 등록 시, 비밀번호 암호화 진행
                memberSignupRequestDTO.getEmail()
        ));

        //throw new ApplicationException(ErrorCode.DUPLICATED_MEMBER_ID, String.format("%s는 이미 존재하는 아이디 입니다.", memberSignupRequestDTO.getMemberId()));

        return MemberDTO.entityToMemberDTO(memberEntity);
    }

//    public String login(String userId, String password) {
//        // 회원가입 여부 체크
//        Optional<MemberEntity> memberEntity = memberRepository.findByMemberId(userId);
//
//        // 비밀번호 체크 (이미 해당 아이디로 가입한 회원이 존재하는 경우)
//        if(memberEntity.isPresent()) {
//            return memberEntity.get().getMemberId();
//        }
//
//        // 토큰 생성
//        return "해당 아이디는 회원가입이 가능합니다.";
//    }

    // 로그인
    @Transactional
    public TokenResponseDTO login(MemberLoginRequestDTO memberLoginRequestDTO, HttpServletResponse response) {

        System.out.println("memberId : " + memberLoginRequestDTO.getMemberId());
        System.out.println("password : " + memberLoginRequestDTO.getPassword());

        // 회원가입 여부 체크
        MemberEntity memberEntity = memberRepository.findByMemberId(memberLoginRequestDTO.getMemberId()).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s는 가입되어있지않은 ID입니다.", memberLoginRequestDTO.getMemberId())));

        // 비밀번호 체크
        if(!encoder.matches(memberLoginRequestDTO.getPassword(), memberEntity.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        TokenResponseDTO tokenResponseDTO = jwtProvider.generateTokenByLogin(memberEntity.getMemberId(), memberEntity.getMemberRole(), secretKey);
        response.addHeader("Authorization", tokenResponseDTO.getAccessToken());     // 헤더에 액세스 토큰만 저장.

        return tokenResponseDTO;
    }

//    // 회원 탈퇴
//    public ResponseEntity<ResponseBody> deleteMember(HttpServletRequest request) {
//
//        // request에서 Access 토큰 정보 추출
//        String refreshToken = request.getHeader("Refresh-Token");
//
//        // 리프레스 토큰 유효성 검사
//        if(!jwtTokenProvider)
//
//
//    }
}
