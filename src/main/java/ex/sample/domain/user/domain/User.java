package ex.sample.domain.user.domain;

import ex.sample.domain.model.SoftDeleteEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "nickname", nullable = false, unique = true, length = 12)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static User of(String nickname, String email, String password) {
        User user = new User();

        user.nickname = nickname;
        user.email = email;
        user.password = password;
        user.role = UserRole.USER;

        return user;
    }
}
