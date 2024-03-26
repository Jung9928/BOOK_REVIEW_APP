package com.jung9407.bookreviewapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.model.dto.jwt.CustomMemberDetails;
import com.jung9407.bookreviewapp.model.dto.jwt.ReissueTokenRequest;
import com.jung9407.bookreviewapp.model.dto.jwt.TokenResponseDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberLoginRequestDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberSignupRequestDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.MemberLoginResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.MemberSignupResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ResponseResultCode;

import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.repository.MemberRepository;
import com.jung9407.bookreviewapp.service.MemberService;
import com.jung9407.bookreviewapp.util.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    // 회원가입
    @PostMapping("/signup")
    public ResponseResultCode<MemberSignupResponseDTO> signup(@RequestBody MemberSignupRequestDTO memberSignupRequestDTO) {
        MemberDTO memberDTO = memberService.signup(memberSignupRequestDTO);

        return ResponseResultCode.success(MemberSignupResponseDTO.getMemberField(memberDTO));
    }

    // 회원가입 시, 아이디 중복확인
    @GetMapping("/verifyId/{memberId}")
    public String verifyId(@PathVariable("memberId") String memberId) {
        log.info("memberId : " + memberId);
        MemberDTO memberDTO = memberService.getMemberByMemberId(memberId);

        return memberDTO.getMemberId();
    }

    // 로그인
//    @PostMapping("/login")
//    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
//        MemberEntity memberEntity = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));
//
//        /**
//         * 이메일, 비밀번호를 입력하고 로그인 요청이 들어오면
//         * memberId 값을 토큰화해서 얻은 토큰을 쿠키에 저장하고
//         * responseEntity 인스턴스인 res에 추가.
//         * */
//        if(memberEntity != null) {
//            String memberId = memberEntity.getMemberId();
//            String token = jwtService.getToken("id", memberId);
//
//            Cookie cookie = new Cookie("token", token);
//            cookie.setHttpOnly(true);       // javascript 로는 접근하지 못하도록 설정
//            cookie.setPath("/");
//
//            res.addCookie(cookie);
//
//            return new ResponseEntity<>(memberId, HttpStatus.OK);
//        }
//
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//    }


    // 로그인
    @PostMapping("/login")
    public ResponseResultCode<MemberLoginResponseDTO> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO, HttpServletResponse response) {
        TokenResponseDTO tokenResponseDTO = memberService.login(memberLoginRequestDTO, response);
        log.info("access token : " + tokenResponseDTO.getAccessToken());
        log.info("refresh token : " + tokenResponseDTO.getRefreshToken());

        return ResponseResultCode.success(new MemberLoginResponseDTO(null, tokenResponseDTO.getRefreshToken()));
//        return new ResponseEntity<>("Login Success", HttpStatus.OK);
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request, @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        String accessToken = jwtProvider.getTokenFromHeader(request);
//        String memberId = (String)jwtProvider.getMemberInfoFromToken(accessToken).get("sub");     // 방법 1
        String memberId = customMemberDetails.getUsername();                                        // 방법 2

        log.info("access token : " + accessToken);
        log.info("memberId : " + memberId);
        return memberService.logout(accessToken, memberId);        // getUsername() : memberId
    }

    @GetMapping("/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtProvider.getMemberInfoFromToken(token);

        if(claims != null) {
            long id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * 해당 유저의 정보 확인
     * @param customMemberDetails
     * @return
     * */
    @GetMapping("/member-info")
    public MemberDTO getMemberInfo(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        return memberService.getMemberByMemberId(customMemberDetails.getUsername());
    }

    /**
     * Access Token 재발급
     * 매 API 호출 시, Security Filter를 통해 인증/인가를 받음.
     * -> 만료된 토큰인지 검증하고 만료 시, 만료된 토큰임을 에러메세지 출력
     * -> 클라이언트에서 에러메세지 확인 후, 이 api(atk 재발급) 요청 진행
     *
     * @param customMemberDetails
     * @param : tokenRequest  refreshToken
     * @return AccessToken + RefreshToken
     * */
    @PostMapping("/reissue-token")
    public TokenResponseDTO reissueToken(@AuthenticationPrincipal CustomMemberDetails customMemberDetails, @RequestBody ReissueTokenRequest reissueTokenRequest) {

        // member 객체 정보를 이용하여 토큰 발행
        MemberDTO memberDTO = MemberDTO.entityToMemberDTO(customMemberDetails.getMemberEntity());
        return jwtProvider.reissueAtk(memberDTO.getMemberId(), memberDTO.getMemberRole(), reissueTokenRequest.getRefreshToken());
    }


//    // 회원탈퇴
//    @DeleteMapping("/api/account/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable String memberId, HttpServletRequest request) {
//        log.info("회원탈퇴요청 ID : {}", memberId);
//        log.info("탈퇴 토큰 : {}", request.getHeader("Authorization"));
//        memberService.deleteMember(request);
//
//        return new ResponseEntity<>("회원 탈퇴 성공", HttpStatus.OK);
//    }
}
