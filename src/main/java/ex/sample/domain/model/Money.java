package ex.sample.domain.model;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable // JPA 값객체 어노테이션
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 스펙상 엔티티나 임베디드 타입은 기본 생성자가 필수
public class Money {

    public static final Money ZERO_KRW = Money.ofKRW(BigDecimal.ZERO);
    public static final Money ONE_KRW = Money.ofKRW(BigDecimal.ONE);

    public static final Money ZERO_USD = Money.ofUSD(BigDecimal.ZERO);
    public static final Money ONE_USD = Money.ofUSD(BigDecimal.ONE);

    private BigDecimal amount;
    private Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new NullPointerException("금액과 통화는 널이 될 수 없습니다.");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money ofKRW(BigDecimal amount) {
        return of(amount, Currency.KRW);
    }

    public static Money ofUSD(BigDecimal amount) {
        return of(amount, Currency.USD);
    }

    public Money add(Money other) {
        checkCurrencyMatch(other);
        return Money.of(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        checkCurrencyMatch(other);
        return Money.of(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(BigDecimal multiplier) {
        return Money.of(this.amount.multiply(multiplier), this.currency);
    }

    private void checkCurrencyMatch(Money other) {
        if (other == null) {
            throw new NullPointerException("금액과 통화는 널이 될 수 없습니다.");
        }
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("통화가 다릅니다.");
        }
    }

    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency;
    }
}
