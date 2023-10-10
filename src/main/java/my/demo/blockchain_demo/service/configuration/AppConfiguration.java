package my.demo.blockchain_demo.service.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Validated
@ConfigurationProperties(prefix = "app")
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class AppConfiguration {
    @NotEmpty
    private final String rpcUrl;
    @NotEmpty
    private final String oracleAddress;
    @NotEmpty
    private final String oraclePrivateKey;
    @NotEmpty
    private final String smartContractAddress;
    @NotNull
    private final long chainId;
    @NotEmpty
    private final List<TreasuryInfo> treasuries;
    @NotEmpty
    private final List<CoinInfo> coins;

    public CoinInfo coin(String currency) {
        return coins.stream().filter(c -> c.name().equals(currency))
                .findFirst().orElse(null);
    }

}
