package my.demo.blockchain_demo.service.startup;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.core.BlockchainAccessor;
import my.demo.blockchain_demo.service.core.domain.MakeTrade;
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
        var txHash = "0x0958718079969fbe3b6abd35a9994264c732a093385d5c569a88af181aec1428";
        var makeTradeData = accessor.getMakeTradeData(txHash);
    }
}