package my.demo.blockchain_demo.service.startup;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.core.BlockchainAccessor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final BlockchainAccessor accessor;

    @SneakyThrows
    @Override
    public void onApplicationEvent(@NotNull final ApplicationReadyEvent event) {
        // step 1
        //var txHash = accessor.callMakeTrade("matic", 1, 1, 1);
        // step 2
        //var txHash = "0xd7146b0d67240b53ba8b9fb686422a1759fb83a8bf28f520c3a97713dd615c7d";
        //var makeTradeData = accessor.getMakeTradeData(txHash);
        // step 3
        var approvedCurrencies = accessor.getApprovedCurrencies();
        log.info("Approved currecncies: {}", approvedCurrencies);
    }
}