package my.demo.blockchain_demo.service.core.domain;

import java.math.BigDecimal;


public record MakePayout(
        String recipient,
        String currency,
        BigDecimal amount,
        String uniqueId
) {
}
