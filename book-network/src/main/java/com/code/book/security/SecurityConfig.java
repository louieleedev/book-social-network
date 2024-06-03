package com.code.book.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * cors(withDefaults()):
     *  이 설정은 모든 출처(Cross-Origin)에서 오는 요청을 허용합니다.
     *  기본 설정을 사용하여 브라우저 보안 정책을 만족시키면서 다른 도메인/출처에서 자원을 요청할 수 있게 합니다.
     * -
     * csrf(AbstractHttpConfigurer::disable):
     *  CSRF(Cross-Site Request Forgery, 사이트 간 요청 위조) 공격으로부터 보호하는 기능을 비활성화합니다.
     *  일반적으로 REST-API 는 상태를 유지하지 않으므로 CSRF 보호가 필요 없습니다.
     * -
     * authorizeHttpRequests(ref -> ...): 특정 경로에 대한 HTTP 요청의 보안을 설정합니다:
     *   1) .requestMatchers(...):
     *          특정 경로들(/auth/**, /v2/api-docs, 등)을 나열하고, .permitAll()을 사용하여 이 경로들에 대한 접근을 인증 없이 허용합니다.
     *          이러한 경로들은 주로 인증 경로 및 Swagger API 문서 관련 경로로, 누구나 접근할 수 있어야 합니다.
     *   2) .anyRequest().authenticated():
     *          위에서 지정한 경로들을 제외한 모든 다른 요청들은 인증된 사용자(로그인한 사용자)만 접근할 수 있도록 설정합니다.
     * -
     * sessionManagement(session -> session.sessionCreationPolicy(STATELESS)):
     *  세션 정책을 STATELESS 로 설정하여 서버가 사용자의 상태(세션)를 유지하지 않도록 합니다. 이 설정은 주로 토큰 기반 인증(JWT 등)을 사용할 때 적합합니다.
     * -
     * authenticationProvider(authenticationProvider):
     *  사용자 인증을 처리할 때 사용할 AuthenticationProvider 를 지정합니다. 이는 사용자가 제공한 자격 증명(아이디, 패스워드 등)을 검증하는 역할을 합니다.
     * -
     * addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class):
     *  jwtAuthFilter 라는 필터를 UsernamePasswordAuthenticationFilter 이전에 추가합니다.
     *  이 필터는 HTTP 요청의 헤더에 포함된 JWT 토큰을 검사하여 사용자 인증을 수행합니다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"
                        ).permitAll()
                                .anyRequest()
                                    .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
