package my.demo.blockchain_demo.configuration;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import my.demo.blockchain_demo.core.wallet.Wallet;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.core.rpc.HttpService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.RawTransactionManager;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class WalletConfiguration {
    private final AppConfiguration appConfiguration;

    @Bean
    public Function<Integer, Wallet> treasuryFactory(Web3j web3j) {
        return treasury -> newTreasuryWallet(web3j, treasury);
    }

    public Wallet newTreasuryWallet(Web3j web3j, int treasury) {
        Preconditions.checkArgument(0 <= treasury && treasury < appConfiguration.treasuries().size(),
                "Not valid treasury");
        var treasuryInfo = appConfiguration.treasuries().get(treasury);
        var chainId = appConfiguration.chainId();
        var credentials = Credentials.create(treasuryInfo.privateKey());
        var txManager = new RawTransactionManager(web3j, credentials, chainId);
        return new Wallet(web3j, appConfiguration.walletContractAddress(), txManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public Web3j web3jClient() {
        return new EthJsonRpcExt(new HttpService(appConfiguration.rpcUrl()));
    }


    @Bean
    public Wallet readOnlyWallet(Web3j web3j) {
        return new Wallet(web3j, appConfiguration.walletContractAddress(), appConfiguration.oracleAddress());
    }

    @Bean
    public Wallet oracleWallet(Web3j web3j) {
        var chainId = appConfiguration.chainId();
        var credentials = Credentials.create(appConfiguration.oraclePrivateKey());
        var txManager = new RawTransactionManager(web3j, credentials, chainId);
        return new Wallet(web3j,appConfiguration.walletContractAddress(), txManager);
    }

}

