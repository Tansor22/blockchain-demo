package my.demo.blockchain_demo.service.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

@Validated
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class TreasuryInfo {
    private final String address;
    private final String privateKey;
}
