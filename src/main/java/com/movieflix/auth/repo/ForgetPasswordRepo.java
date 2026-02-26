package com.movieflix.auth.repo;

import com.movieflix.auth.entity.ForgetPassword;
import com.movieflix.auth.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface ForgetPasswordRepo extends JpaRepository<ForgetPassword, Integer> {
    @Query("select fp from ForgetPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgetPassword> findByOtpAndUser(Integer otp, User user);

    // في ForgetPasswordRepo
    @Modifying
    @Transactional
    @Query("DELETE FROM ForgetPassword fp WHERE fp.fpid = :fpid")
    void deleteByFpid(@Param("fpid") Integer fpid);
}
