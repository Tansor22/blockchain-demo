package my.demo.blockchain_demo.configuration;

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

    public BigDecimal amountFromHex(String hex) {
        var cleanHex = hex.startsWith("0x") ? hex.substring(0, 2) : hex;
        var bi = new BigInteger(cleanHex, 16);
        return new BigDecimal(bi).movePointLeft(decimals);
    }

    public BigInteger amount(double d) {
        return amount(BigDecimal.valueOf(d));
    }

    public BigInteger amount(BigDecimal bd) {
        return bd.movePointRight(decimals).toBigInteger();

    }
    public BigDecimal amount(BigInteger bi) {
        return new BigDecimal(bi).movePointLeft(decimals);
    }

    public BigInteger minEncoded() {
        return amount(min);
    }

    public BigInteger maxEncoded() {
        return amount(max);
    }
}
