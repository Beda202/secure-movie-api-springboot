package com.movieflix.auth.entity;

import com.movieflix.auth.Enum.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId ;

    @NotBlank(message = "Name can not be blank")
    private String name ;

    @NotBlank(message = "UserName can not be blank")
    @Column(unique = true)
    private String userName ;

    @NotBlank(message = "Email can not be blank")
    @Column(unique = true)
    @Email(message = "Please enter Email in proper format !")
    private String email ;

    @NotBlank(message = "Password can not be blank")
    @Size(min = 5 , message = "The Password must contain at least 5 characters")
    private String password ;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    private ForgetPassword forgetPassword;

    private boolean isAccountNonExpired=true ;
    private boolean isAccountNonLocked=true ;
    private boolean isCredentialsNonExpired=true ;
    private boolean isEnabled=true ;


    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(role.name())
        );
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
