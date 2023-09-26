package my.demo.blockchain_demo.service.configuration;

import lombok.RequiredArgsConstructor;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.rpc.HttpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RpcClientConfiguration {
    private final AppConfiguration appConfiguration;

    @Bean
    public EthJsonRpcExt web3jClient() {
        return new EthJsonRpcExt(new HttpService(appConfiguration.rpcUrl()));
    }
}
