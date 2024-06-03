package com.code.book.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Token 클래스는 사용자 인증 시스템에서 토큰 관리를 위한 엔티티를 나타낸다. 이 클래스는 JPA를 사용하여 데이터베이스에 저장된다.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue
    private Integer id;                             // id: 토큰의 고유 식별자로, 데이터베이스에서 자동으로 생성되는 값.
    private String token;                           // 토큰 값을 저장하는 필드. 이 값은 일반적으로 사용자 인증 시 보안 검증에 사용.
    private LocalDateTime createdAt;                // 토큰이 생성된 시각
    private LocalDateTime expiresAt;                // 토큰의 만료 시각 (이 시간이 지나면 토큰은 유효하지 않음)
    private LocalDateTime validatedAt;              // 토큰이 검증(사용)된 시각

    /**
     * user: Token은 User에 속합니다. @ManyToOne 애노테이션은 다대일 관계를 나타내며, 한 명의 사용자가 여러 개의 토큰을 가질 수 있음을 의미.
     * 애노테이션 @JoinColumn(name = "userId", nullable = false): 이 토큰이 속한 사용자의 ID를 userId 필드에 저장.
     * nullable = false는 이 필드가 비어 있지 않아야 함을 의미하며, 토큰이 반드시 하나의 사용자와 연결되어야 함을 보장.
     */
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
