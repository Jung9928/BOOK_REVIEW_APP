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
@RequestMapping("/api/v1/verifyEmail")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    // 이메일 인증 버튼을 눌러 인증메일을 발송하는 api
    @GetMapping("/{emailAddr:.+}")
    public ResponseEntity sendVerifyEmail(@PathVariable String emailAddr) throws MessagingException {
        log.info("emailAddr : {}", emailAddr);
        emailService.sendMail(emailAddr);

        return ResponseEntity.ok().build();
    }

    // 가입자가 이메일 인증번호를 받은 후, 해당 이메일 인증번호 유효 여부를 검증하는 api
    @PostMapping("/{emailAddr:.+}")
    public ResponseEntity<String> sendEmailAndCode(@PathVariable String emailAddr, @RequestBody EmailRequestDTO emailRequestDTO) throws NoSuchAlgorithmException {
        log.info("emailAddr : {}", emailAddr);
        log.info("verifyCode : {}", emailRequestDTO.getVerificationCode());
        if(emailService.verifyEmailCode(emailAddr, emailRequestDTO.getVerificationCode())) {
            return ResponseEntity.ok(emailService.makeMemberId(emailAddr));
        }
        return ResponseEntity.notFound().build();
    }
}

//669ed28c8415b46faf4ae30bcf22a830c589b68158d6116570f86c91c0b8df81
