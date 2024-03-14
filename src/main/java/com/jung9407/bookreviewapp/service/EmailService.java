package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.util.RedisMailUtil;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisMailUtil redisMailUtil;

    @Value("${spring.mail.username}")
    private String verifyEmailSender;

    // 이메일 인증번호 생성 메소드
    public String createEmailVerifyCode() {
        int verifyCodeLength = 6;

        Random random = new Random();

        // ASCII 코드로 숫자 '0'부터 알파벳 'z' 중에서 랜덤 추출하여 난수 생성
        return random.ints(48, 122)
                .filter(i -> (i <= 57 || i>=65) && (i <= 90 || i>= 97))
                .limit(verifyCodeLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // MimeMessage 객체 안에 코드, 송신 이메일, Context를 담고 난수와 이메일은 Redis에 저장
    private MimeMessage createEmailForm(String email) throws MessagingException {

        String verifyCode = createEmailVerifyCode();

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("도서리뷰모아 이메일 인증 번호입니다.");
        message.setFrom(verifyEmailSender);                                             // 인증 안내 메일 보내는 사랑
        message.setText("이메일 인증코드 : " + verifyCode, "UTF-8", "html");

        redisMailUtil.setDataExpire(email, verifyCode, 60 * 3L);   // 인증번호 유효시간 3분

        return message;
    }

    // 생성한 메일을 전송하는 메소드. Redis에 해당 이메일로 된 key 값이 있다면 Redis db에서 삭제 후, 전송
    public void sendMail(String toEmail) throws MessagingException {
        if(redisMailUtil.existData(toEmail)) {
            redisMailUtil.deleteData(toEmail);
        }

        MimeMessage emailForm = createEmailForm(toEmail);
        javaMailSender.send(emailForm);
    }

    // 전송한 이메일과 코드가 일치하는지 redis에 key(이메일)값을 전달하여 value(인증코드) 검증
    public Boolean verifyEmailCode(String email, String verifyCode) {
        String foundVerifyCodeFromRedis = redisMailUtil.getData(email);

        if(foundVerifyCodeFromRedis == null) {
            return false;
        }
        return foundVerifyCodeFromRedis.equals(verifyCode);
    }

    // 모든 인증에 성공했을 경우, memberId 생성 후 리턴
    // 해쉬암호화 알고리즘 사용 : SHA-256
    public String makeMemberId(String email) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(email.getBytes());
        md.update(LocalDateTime.now().toString().getBytes());

        StringBuilder sb = new StringBuilder();

        for(byte b : md.digest()) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
