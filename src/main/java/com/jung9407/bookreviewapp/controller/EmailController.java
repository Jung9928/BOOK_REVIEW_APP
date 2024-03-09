package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.requestDTO.EmailRequestDTO;
import com.jung9407.bookreviewapp.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    // 이메일 인증 버튼을 눌러 인증메일을 발송하는 api
    @GetMapping("/{emailAddr:.+}")
    public ResponseEntity<String> sendVerifyEmail(@PathVariable String emailAddr) throws MessagingException {
        log.info("Inside sendVerifyEmail method"); // 추가
        log.info("emailAddr : {}", emailAddr);
        emailService.sendMail(emailAddr);
        return ResponseEntity.ok("이메일을 확인하세요");
    }

    // 이메일 인증번호를 받은 후, 해당 이메일 인증번호를 검증하는 api
    @PostMapping("/verifycode/{emailAddr}")
    public ResponseEntity<String> sendEmailAndCode(@PathVariable String emailAddr, @RequestBody EmailRequestDTO emailRequestDTO) throws NoSuchAlgorithmException {
        log.info("emailAddr : {}", emailAddr);
        if(emailService.verifyEmailCode(emailAddr, emailRequestDTO.getCode())) {
            return ResponseEntity.ok(emailService.makeMemberId(emailAddr));
        }
        return ResponseEntity.notFound().build();
    }
}
