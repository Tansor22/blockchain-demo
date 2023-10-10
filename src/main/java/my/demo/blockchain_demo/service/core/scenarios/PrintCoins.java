package my.demo.blockchain_demo.service.core.scenarios;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.scenarios.shared.Scenario;
import my.demo.blockchain_demo.service.core.utils.CommonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@ConditionalOnProperty(value = "scenario", havingValue = "printCoins")
public class PrintCoins implements Scenario {
    private final AppConfiguration appConfiguration;

    @Override
    public void go() throws Exception {
        for (var coin : appConfiguration.coins()) {
            var name = CommonUtils.toHex32String(coin.name(), true);
            var address = coin.address();
            var min = coin.minEncoded();
            var max = coin.maxEncoded();
            log.trace("Coin: {}, name: {}, address: {}, min: {}, max: {}",
                    CommonUtils.fromHex32String(name), name, address, min, max
            );
        }

    }
}
