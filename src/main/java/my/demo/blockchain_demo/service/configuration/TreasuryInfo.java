package my.demo.blockchain_demo.service.configuration;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

@Validated
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class TreasuryInfo {
    @NotEmpty
    private final String address;
    @NotEmpty
    private final String privateKey;
}
