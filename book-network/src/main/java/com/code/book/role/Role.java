package com.code.book.role;

import com.code.book.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Role 클래스는 사용자가 가질 수 있는 역할(권한)을 나타내는 엔티티다.
 * 각 역할은 고유한 이름을 가지며, 하나 이상의 사용자에게 할당될 수 있다.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                                         // 역할 ID 로써 역할의 고유 식별자
                                                                // 자동으로 생성되며, ID 생성 전략은 데이터베이스의 ID 자동 증가 기능을 사용

    @Column(unique = true)
    private String name;                                        // 역할의 이름으로, 유일한 값이어야 한다

    /**
     *  이 역할을 가진 사용자 목록. @ManyToMany 관계이며, User 클래스의 roles 필드와 연결.
     *  ManyToMany(mappedBy = "roles") 애노테이션은 User 클래스와 Role 클래스 사이에 다대다(n-n) 관계를 정의.
     *  mappedBy = "roles" 속성은 Role 클래스가 User 클래스의 roles 필드에 의해 매핑된다는 것을 나타냄.
     *  즉, User 클래스에서 roles 필드가 이미 관계를 소유하고 있으며, Role 클래스는 이 관계에 종속적이라는 의미.
     *  mappedBy 속성을 사용함으로써 양방향 연관 관계에서 주체가 되는 쪽을 명확히 하고, 불필요한 중복 매핑을 피할 수 있음.
     *  -
     *  JsonIgnore 애노테이션은 JSON 직렬화 과정에서 이 필드를 무시하도록 지시한다.
     *  즉, Role 객체를 JSON으로 변환할 때 users 목록은 포함되지 않습니다. 이는 무한 재귀나 성능 문제를 방지하는 데 유용함.
     *  예를 들어, API를 통해 Role 데이터를 클라이언트에게 보낼 때, 연결된 모든 User 정보를 보내는 것은 불필요하거나 보안상의 문제가 될 수 있기 때문입니다.
     */
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDated;                         // 역할이 생성자 날짜

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDated;                    // 역할이 수정된 날짜
}
