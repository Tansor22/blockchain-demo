package my.demo.blockchain_demo.startup;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.core.scenarios.shared.Scenario;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final Scenario scenario;

    @SneakyThrows
    @Override
    public void onApplicationEvent(@NotNull final ApplicationReadyEvent event) {
        log.info("Scenario: {}", scenario.getClass().getSimpleName());
        scenario.go();
    }
}