package my.demo.blockchain_demo.service.core;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
public class AmountsParser {
    private final Map<String, Integer> CURRENCIES_BY_PRECISION =
            // todo precision for matic ??
            Map.of("usdt", 6, "matic", 18);

    public @Nullable BigDecimal parseAmount(Type abi, String currency) {
        var precision = CURRENCIES_BY_PRECISION.get(currency.toLowerCase());
        if (precision == null) {
            log.error("Currency {} is not supported.", currency);
        } else {
            if (abi instanceof Uint256 amount) {
                var rawValue = amount.getValue();
                // should shift by some number
                return new BigDecimal(rawValue).movePointLeft(precision);
            } else {
                log.error("Incorrect type for input {}, should be uint256.", abi.getTypeAsString());
            }
        }
        return null;
    }
}
