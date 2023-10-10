package my.demo.blockchain_demo.service.configuration;

import com.google.common.base.Preconditions;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.BigInteger;

@Validated
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class CoinInfo {
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String address;
    @Positive
    private final int decimals;
    @Positive
    private final BigDecimal min;
    @Positive
    private final BigDecimal max;

    public BigInteger amount(double d) {
        return amount(BigDecimal.valueOf(d));
    }
    public BigInteger amount(BigDecimal bd) {
        Preconditions.checkArgument(
                bd.compareTo(min) >= 0 && bd.compareTo(max) <= 0,
                "Amount %s should be %s <= 'amount' <= %s", bd, min, max);
        return bd.movePointRight(decimals).toBigInteger();
    }

    public BigInteger minEncoded() {
        return amount(min);
    }

    public BigInteger maxEncoded() {
        return amount(max);
    }
}
