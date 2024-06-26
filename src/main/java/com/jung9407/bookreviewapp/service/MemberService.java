package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.exception.ApplicationException;
import com.jung9407.bookreviewapp.exception.ErrorCode;
import com.jung9407.bookreviewapp.model.dao.RedisDAO;
import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberDeleteRequestDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberLoginRequestDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberModifyRequestDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberSignupRequestDTO;
import com.jung9407.bookreviewapp.model.dto.jwt.TokenResponseDTO;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.repository.MemberRepository;
import com.jung9407.bookreviewapp.util.CookieUtils;
import com.jung9407.bookreviewapp.util.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private final RedisDAO redisDAO;

    private final BCryptPasswordEncoder encoder;

    private final CookieUtils cookieUtils;

    // ====================================================
    // 추후, 변경 용이를 위해 yml 또는 properties 파일에서
    // jwt 값을 @Value를 사용하여 yml파일로부터 가져오도록 구현
    // ====================================================
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.live.atk}")
    private int accessToken;           // millisecond


//    public void signup(MemberJoinDTO memberJoinDTO) {
//        // 1. dto -> entity로 변환
//        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberJoinDTO);
//
//        // 2. repository의 save 메소드 호출 (조건, entity객체를 넘겨줘야 함)
//        memberRepository.save(memberEntity);
//    }

    // ID 정보 가져오기
    public MemberDTO getMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).map(MemberDTO::entityToMemberDTO).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s는 존재하지 않는 ID 입니다.", memberId)));
    }


    // Email 정보 가져오기
    public MemberDTO getMemberByMemberEmail(String memberId) {
        return memberRepository.findEmailByMemberId(memberId).map(MemberDTO::entityToMemberDTO).orElseThrow(() ->
                new ApplicationException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s는 존재하지 않는 Email 입니다.", memberId)));
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
                memberSignupRequestDTO.getEmail(),
                memberSignupRequestDTO.getMemberRole()
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

        log.info("memberId : " + memberLoginRequestDTO.getMemberId());
        log.info("password : " + memberLoginRequestDTO.getPassword());

        // ========================
        // 1. 로그인 과정에서 회원정보 검증 작업
        // ========================
        // 회원가입 여부 체크
        MemberEntity memberEntity = memberRepository.findByMemberId(memberLoginRequestDTO.getMemberId()).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s는 가입되어있지않은 ID입니다.", memberLoginRequestDTO.getMemberId())));

        // 비밀번호 체크
        if(!encoder.matches(memberLoginRequestDTO.getPassword(), memberEntity.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // ================================
        // 2. 회원정보 검증결과 정상일 경우 작업
        // ================================
        // 토큰 생성
        TokenResponseDTO tokenResponseDTO = jwtProvider.generateTokenByLogin(memberEntity.getMemberId(), memberEntity.getMemberRole(), secretKey);

//        log.info("accessToken 만료 시간 : " );
        // 쿠키 생성 후, refresh 토큰을 쿠키에 저장 (2024-03-23 추가)
        // accessToken 헤더 명 : Authorization
        // refreshToken 쿠키 명 : Refresh

//        Cookie accessToken = cookieUtils.createCookie("Access", tokenResponseDTO.getAccessToken());
        Cookie refreshToken = cookieUtils.createCookie("Refresh", tokenResponseDTO.getRefreshToken());


        // 쿠키를 response에 담아서 리턴 (2024-03-23 추가)
//        response.addCookie(accessToken);
        // header에 accessToken 저장
        response.addHeader("Authorization", tokenResponseDTO.getAccessToken());     // 헤더에 Access 토큰만 저장.
        response.addCookie(refreshToken);

        return tokenResponseDTO;
    }

    // 로그아웃
    @Transactional
    public ResponseEntity logout(String accessToken, HttpServletResponse response, String memberId) {
        // Redis에 accessToken을 사용못하도록 blacklist 등록
        log.info("============ logout 시작 ==========");
        log.info("전달받은 accessToken : " + accessToken);
        log.info("전달받은 memberId : " + memberId);
        Long expiration = jwtProvider.getExpirationTime(accessToken);
        redisDAO.setBlackList(accessToken, "logout", expiration);

        if(redisDAO.hasKey(memberId)) {
            Cookie refreshToken = cookieUtils.deleteCookie("Refresh");
            response.addCookie(refreshToken);

            redisDAO.deleteRefreshToken(memberId);
        }
        else {
            throw new IllegalArgumentException("이미 로그아웃한 유저입니다.");
        }
        return ResponseEntity.ok("로그아웃 완료");
    }


    // 회원정보수정
    @Transactional
    public MemberDTO modifyInfo(MemberModifyRequestDTO memberModifyRequestDTO) {

        // 1. 회원 존재 여부 체크(기존 회원 정보 가져오기)
        MemberEntity memberEntity = memberRepository.findByMemberId(memberModifyRequestDTO.getMemberId()).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s는 가입되어있지않은 ID입니다.", memberModifyRequestDTO.getMemberId())));

        // 2. 새로운 비밀번호 설정
        String newPassword = encoder.encode(memberModifyRequestDTO.getPassword());

        // 3. 회원정보 수정 진행 (Dirty Checking)
        memberEntity.setPassword(newPassword);
        memberEntity.setEmail(memberModifyRequestDTO.getEmail());
        memberEntity.setMemberRole(memberModifyRequestDTO.getMemberRole());

        // 4. 수정된 회원 정보 저장
        MemberEntity updatedMemberEntity = memberRepository.save(memberEntity);

        return MemberDTO.entityToMemberDTO(updatedMemberEntity);
    }

    // 회원 탈퇴
    @Transactional
    public ResponseEntity deleteMember(MemberDeleteRequestDTO memberDeleteRequestDTO) {

        // 1. 회원가입 여부 체크
        MemberEntity memberEntity = memberRepository.findByMemberId(memberDeleteRequestDTO.getMemberId()).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s는 가입되어있지않은 ID입니다.", memberDeleteRequestDTO.getMemberId())));

        // 2. 탈퇴 과정에서 입력한 비밀번호 검증
        if(!encoder.matches(memberDeleteRequestDTO.getPassword(), memberEntity.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 3. redis에 accessToken이 blackList로 등록되어있는지 확인 -> 등록되어있다면 탈퇴 불가 (재로그인 후, 탈퇴 진행)
        //    refresh 토큰도 삭제 진행.
        if(redisDAO.hasKey(memberDeleteRequestDTO.getAccessToken())) {
            redisDAO.deleteBlackList(memberDeleteRequestDTO.getAccessToken());
            redisDAO.deleteRefreshToken(memberDeleteRequestDTO.getMemberId());
        }

        memberRepository.deleteByMemberId(memberDeleteRequestDTO.getMemberId());

        return ResponseEntity.ok("회원 탈퇴 완료");
    }
}
