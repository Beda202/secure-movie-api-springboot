package com.movieflix.controller;

import com.movieflix.auth.entity.ForgetPassword;
import com.movieflix.auth.entity.User;
import com.movieflix.auth.repo.ForgetPasswordRepo;
import com.movieflix.auth.repo.UserRepo;
import com.movieflix.auth.util.ChangePassword;
import com.movieflix.dto.MailBody;
import com.movieflix.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forget-password")
public class ForgetPasswordController {
    private final UserRepo userRepository ;
    private final EmailService emailService ;
    private final ForgetPasswordRepo forgetPasswordRepo ;
    private final PasswordEncoder passwordEncoder ;


    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyMail(@PathVariable String email){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Please enter valid email"));
        int otp = generateOTP();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("This is OTP for forget password" + otp)
                .text("Please click on the link to reset your password")
                .build();

        ForgetPassword forgetPassword = ForgetPassword.builder()
                .otp(otp)
                .expiredAt(new Date(System.currentTimeMillis() +70 *1000))
                .user(user)
                .build();
        emailService.sendSimpleMessage(mailBody);
        forgetPasswordRepo.save(forgetPassword);
        return ResponseEntity.ok("Email verification initiated");
    }
    @Transactional
    @PostMapping("/verifyOTP/{otp}/{email}")
    public ResponseEntity<String> verifyOTP(@PathVariable Integer otp, @PathVariable String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Please enter valid email"));
        ForgetPassword forgetPassword = forgetPasswordRepo.findByOtpAndUser(otp, user).orElseThrow(() -> new UsernameNotFoundException("Please enter valid email" + email));
        if(forgetPassword.getExpiredAt().before(Date.from(Instant.now()))){
            user.setForgetPassword(null);
            forgetPasswordRepo.deleteById(forgetPassword.getFpid());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("OTP expired");
        }
        return ResponseEntity.ok("OTP verified successfully");

    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePassword(@RequestBody ChangePassword changePassword, @PathVariable String email){
        if(!Objects.equals(changePassword.password(), changePassword.confirmPassword())){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Password does not match");
        }
        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(encodedPassword, email);
        return ResponseEntity.ok("Password changed successfully");
    }

    private Integer generateOTP(){
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
