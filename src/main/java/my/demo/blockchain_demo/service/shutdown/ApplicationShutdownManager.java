package my.demo.blockchain_demo.service.shutdown;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationShutdownManager {
    private final ApplicationContext appContext;

    public void initiateShutdown() {
        initiateShutdown(0);
    }

    public void initiateShutdown(int returnCode) {
        SpringApplication.exit(appContext, () -> {
            log.info("Application shutdown.");
            return returnCode;
        });
    }
}
