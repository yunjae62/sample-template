package ex.sample.domain.model;

import jakarta.persistence.Embeddable;
import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable // JPA 값객체 어노테이션
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 스펙상 엔티티나 임베디드 타입은 기본 생성자가 필수
public class Money {

    public static final Money ZERO = new Money(BigInteger.ZERO);
    public static final Money ONE = new Money(BigInteger.ONE);
    public static final Money TEN = new Money(BigInteger.TEN);

    private BigInteger amount;

    public Money(BigInteger amount) {
        if (amount == null) {
            throw new NullPointerException("금액은 널이 될 수 없습니다.");
        }
        this.amount = amount;
    }

    public Money add(Money other) {
        checkNull(other);
        return new Money(amount.add(other.amount));
    }

    public Money subtract(Money other) {
        checkNull(other);
        return new Money(amount.subtract(other.amount));
    }

    public Money multiply(long multiplier) {
        return new Money(amount.multiply(BigInteger.valueOf(multiplier)));
    }

    private void checkNull(Money money) {
        if (money == null) {
            throw new NullPointerException("금액은 널이 될 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
