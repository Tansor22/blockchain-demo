package my.demo.blockchain_demo.service.core.contract.events;

import org.web3j.abi.datatypes.Event;

import java.util.List;

public interface DeFiEvent {
    String name();
    List<DeFiEventsParam> params();

    default Event toEvent() {
        List params = params().stream()
                .map(DeFiEventsParam::type)
                .toList();
        return new Event(name(), params);
    }
}
