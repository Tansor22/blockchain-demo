package my.demo.blockchain_demo.core.scenarios;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.core.scenarios.shared.Scenario;
import my.demo.blockchain_demo.core.wallet.Wallet;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "scenario", havingValue = "wallet")
public class WalletScenario implements Scenario {
    private final Wallet readOnlyWallet;
    private final AppConfiguration appConfiguration;

    @Override
    public void go() throws Exception {
        var oracleAddress = readOnlyWallet.getOracleAddress();
        log.trace("Oracle address: {}", oracleAddress);
        log.trace("Balances: ");
        for (var coin: appConfiguration.coins()) {
            // todo works only for matic, should check tokens
            var rawBalance = readOnlyWallet.getBalance("matic");
            log.trace("{} -> {}", coin.name(), coin.amount(rawBalance));
        }


    }
}
