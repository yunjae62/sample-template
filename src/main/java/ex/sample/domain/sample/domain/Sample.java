package ex.sample.domain.sample.domain;

import ex.sample.domain.model.BaseEntity;
import ex.sample.domain.model.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
@Table(name = "samples") // 컨벤션에 맞게 적용, 여기서는 복수형으로 표현
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE samples SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 엔티티는 기본 생성자가 필수라서 접근 제어자 최소화
public class Sample extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Embedded
    private Money money;

    public static Sample create(String name, Money money) {
        Sample sample = new Sample();

        sample.name = name;
        sample.money = money;

        return sample;
    }
}
