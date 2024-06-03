package com.code.book.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {

    /**
     * UserDetailsService 는 스프링 시큐리티에서 사용자의 상세 정보를 로드하는 인터페이스입니다.
     * 사용자 정의 서비스에서 이 인터페이스를 구현하여 사용자 정보를 데이터베이스 등에서 조회할 수 있습니다.
     */
    private final UserDetailsService userDetailsService;

    /**
     * authenticationProvider():
     *  -   AuthenticationProvider 는 스프링 시큐리티의 인증 메커니즘을 추상화한 인터페이스입니다.
     *      이 메소드는 DaoAuthenticationProvider 인스턴스를 생성하고, 이 인스턴스를 스프링 보안 인증 과정에 사용합니다.
     *  -   setUserDetailsService(userDetailsService): 인증 과정 중에 사용자 정보를 조회하기 위해 UserDetailsService 를 설정합니다.
     *  -   setPasswordEncoder(passwordEncoder()): 비밀번호 검증을 위해 PasswordEncoder를 설정합니다.
     *      이 메소드는 하단에서 정의된 passwordEncoder() 메소드를 호출하여 BCryptPasswordEncoder 인스턴스를 사용합니다.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * passwordEncoder():
     *  PasswordEncoder 는 비밀번호를 안전하게 처리(암호화)하기 위한 인터페이스입니다.
     *  BCryptPasswordEncoder 는 비밀번호를 해싱하여 저장하는 데 사용되며, 보안적으로 권장되는 방식입니다.
     *  이 빈은 비밀번호를 해시하고, 사용자가 입력한 비밀번호와 저장된 해시를 비교하는 데 사용됩니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
