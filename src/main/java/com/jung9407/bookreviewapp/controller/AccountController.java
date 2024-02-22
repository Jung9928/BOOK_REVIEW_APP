package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberLoginRequestDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.MemberSignupRequestDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.MemberLoginResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.MemberSignupResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ResponseResultCode;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;

import com.jung9407.bookreviewapp.repository.MemberRepository;
import com.jung9407.bookreviewapp.service.JwtService;
import com.jung9407.bookreviewapp.service.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtService jwtService;

//    @Autowired
//    MemberService memberService;

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
    public ResponseResultCode<MemberLoginResponseDTO> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        String token = memberService.login(memberLoginRequestDTO.getUserId(), memberLoginRequestDTO.getPassword());
        return ResponseResultCode.success(new MemberLoginResponseDTO(token));
    }

    // 로그아웃
    @PostMapping("logout")
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        res.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtService.getClaims(token);

        if(claims != null) {
            int id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    // 회원탈퇴
//    @DeleteMapping("/api/account/{id}")
//    public void deleteUser(@PathVariable int id) {
//        MemberEntity.deletedById(id);
//    }
}
