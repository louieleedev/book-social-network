package com.code.book.user;

import com.code.book.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User 클래스는 데이터베이스의 사용자 정보를 나타내는 엔티티 이다.
 * 이 클래스는 Spring Security의 UserDetails와 Principal 인터페이스를 구현하여, 보안 컨텍스트에서 사용될 수 있도록 한다.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue
    private Integer id;                     // 사용자의 ID 고유 식별자. 자동 생성된 값
    private String firstname;               // 사용자의 이름과 성
    private String lastname;
    private LocalDate dateOfBirth;          // 사용자의 생년월일 (LocalDate 타입으로 저장)
    @Column(unique = true)
    private String email;                   // 사용자의 이메일 주소, 유일한 값이어야 함: (@Column(unique = true))
    private String password;                // 사용자의 비밀번호
    private boolean accountLocked;          // 계정 잠김 상태
    private boolean enabled;                // 계정 활성화 상태

    /**
     * 사용자가 가지고 있는 역할 목록. @ManyToMany 관계를 사용하며, Role 클래스와 연결
     * fetch = FetchType.EAGER 이란? -> fetch 속성은 JPA에서 엔티티를 데이터베이스에서 로드할 때 관련된 엔티티를 어떻게 가져올지를 정의.
     * FetchType.EAGER는 "즉시 로딩"을 의미. 즉, 주 엔티티를 로드할 때 연결된 모든 관련 엔티티(이 경우 Role 엔티티)도 함께 로드 됨.
     * 이는 엔티티가 로드될 때 관련된 모든 데이터가 한 번에 데이터베이스로부터 조회되어야 함을 의미하므로, 필요한 모든 정보가 바로 사용 가능.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDated;             // 사용자 계정이 생성자 날짜

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDated;        // 사용자 계정이 수정된 날짜

    @Override
    public String getName() {
        return email;
    }

    /**
     * 사용자에게 부여된 권한들을 반환한다.
     * 이는 Role 에서 권한 이름을 SimpleGrantedAuthority 객체로 변환하여 리스트로 제공합니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    private String getFullName() {
        return firstname + " " + lastname;
    }
}
