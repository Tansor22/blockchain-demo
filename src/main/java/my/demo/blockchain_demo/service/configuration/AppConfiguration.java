package my.demo.blockchain_demo.service.configuration;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;



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
}
