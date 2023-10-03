package my.demo.blockchain_demo.service.core.scenarios.shared;

@FunctionalInterface
public interface Scenario<R> {
    R go() throws Exception;
}
