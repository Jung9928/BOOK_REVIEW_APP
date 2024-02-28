package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.model.dto.jwt.TokenResponseDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberLoginRequestDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberSignupRequestDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.MemberLoginResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.MemberSignupResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ResponseResultCode;

import com.jung9407.bookreviewapp.repository.MemberRepository;
import com.jung9407.bookreviewapp.service.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final MemberService memberService;

    // 회원가입
//    @PostMapping("/api/v1/signup")
//    public ResponseEntity signup(@RequestBody MemberJoinDTO memberJoinDTO) {
//        System.out.println("AccountController.java");
//        System.out.println("memberJoinDTO : " + memberJoinDTO);
//        memberService.signup(memberJoinDTO);
//
//        return new ResponseEntity<>(memberJoinDTO.getMemberId(), HttpStatus.OK);
//    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseResultCode<MemberSignupResponseDTO> signup(@RequestBody MemberSignupRequestDTO memberSignupRequestDTO) {
        MemberDTO memberDTO = memberService.signup(memberSignupRequestDTO);

        return ResponseResultCode.success(MemberSignupResponseDTO.getMemberField(memberDTO));
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


    @PostMapping("/login")
    public ResponseResultCode<MemberLoginResponseDTO> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO, HttpServletResponse response) {
        TokenResponseDTO tokenResponseDTO = memberService.login(memberLoginRequestDTO, response);
        System.out.println("access token : " + tokenResponseDTO.getAccessToken());
        System.out.println("refresh token : " + tokenResponseDTO.getRefreshToken());

        return ResponseResultCode.success(new MemberLoginResponseDTO(tokenResponseDTO.getAccessToken(), tokenResponseDTO.getRefreshToken()));
    }

    // 로그아웃
//    @Transactionfa("logout")
//    public ResponseEntity logout(HttpServletResponse res) {
//        Cookie cookie = new Cookie("token", null);
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//
//        res.addCookie(cookie);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//    @GetMapping("check")
//    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
//        Claims claims = jwtService.getClaims(token);
//
//        if(claims != null) {
//            int id = Integer.parseInt(claims.get("id").toString());
//            return new ResponseEntity<>(id, HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }

    // 로그아웃
//    @PatchMapping("/logout")

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
