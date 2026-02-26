package com.movieflix.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ForgetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpid ;

    @Column(nullable = false)
    private Integer otp ;

    @Column(nullable = false)
    private Date expiredAt ;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user ;
}
