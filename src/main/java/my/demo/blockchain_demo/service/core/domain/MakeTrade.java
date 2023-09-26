package my.demo.blockchain_demo.service.core.domain;

import java.math.BigDecimal;


public record MakeTrade(
        String currency,
        BigDecimal amount,
        String orderId,
        String code,
        String deadline
) {
}