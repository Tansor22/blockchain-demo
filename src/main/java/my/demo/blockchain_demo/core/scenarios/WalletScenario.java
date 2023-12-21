package my.demo.blockchain_demo.core.scenarios;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.configuration.CoinInfo;
import my.demo.blockchain_demo.core.scenarios.shared.Scenario;
import my.demo.blockchain_demo.core.wallet.Wallet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "scenario", havingValue = "wallet")
public class WalletScenario implements Scenario {
    private final Wallet readOnlyWallet;
    private final Function<Integer, Wallet> treasuryWallets;
    private final AppConfiguration appConfiguration;

    public void printCurrenciesInfo() {
        for (var coin : appConfiguration.coins()) {
            var contractCurrency = readOnlyWallet.getApprovedCurrency(coin.name());
            log.trace(
                    "Coin '{}' is {} and {}. Token address: {}. Limits: [{}, {}]", coin.name(),
                    contractCurrency.isEnabled() ? "enabled" : "disabled",
                    contractCurrency.isActive() ? "active" : "inactive",
                    contractCurrency.getAddress(),
                    coin.amount(contractCurrency.getMinAmount()).stripTrailingZeros().toPlainString(),
                    coin.amount(contractCurrency.getMaxAmount()).stripTrailingZeros().toPlainString()
                    // no approvedBy
            );
        }
    }

    public void printOracleAddress() {
        var oracleAddress = readOnlyWallet.getOracleAddress();
        log.trace("Oracle address: {}", oracleAddress);
    }

    public void printBalances() {
        log.trace("Balances: ");
        for (var coin: appConfiguration.coins()) {
            var rawBalance = readOnlyWallet.getBalance(coin.name());
            log.trace("{} -> {}", coin.name(), coin.amount(rawBalance));
        }
    }

    @Override
    public void go() throws Exception {
        var t1wallet = treasuryWallets.apply(0);
        var t2wallet = treasuryWallets.apply(1);
        log.trace("Approved currencies: {} ", readOnlyWallet.getApprovedCurrencies());
        // usdt, dai, link
        //t1wallet.removeApprovedCurrency("link");
        //log.trace("Approved currencies: {} ", readOnlyWallet.getApprovedCurrencies());


        //log.trace("Proposed currencies: {} ", readOnlyWallet.getProposedCurrencies());
        for (var c : appConfiguration.coins()) {
            if ("matic".equalsIgnoreCase(c.name())) {
                continue;
            }
            //t1wallet.proposeCurrency(c.name(), c.address(), c.minEncoded(), c.maxEncoded());
            //t2wallet.approveProposedCurrency(c.name(), c.address(), c.minEncoded(), c.maxEncoded());
        }
        //log.trace("Proposed currencies: {} ", readOnlyWallet.getProposedCurrencies());
        //printOracleAddress();
        printCurrenciesInfo();
        printBalances();


    }
}
