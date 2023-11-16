package my.demo.blockchain_demo.core.contract.events;

import org.web3j.abi.datatypes.Event;

import java.util.Comparator;
import java.util.List;

public interface DeFiEvent {
    String name();
    List<DeFiEventsParam> params();

    default Event toEvent() {
        List params = params().stream()
                .sorted(Comparator.comparingInt(DeFiEventsParam::index))
                .map(DeFiEventsParam::type)
                .toList();
        return new Event(name(), params);
    }
}
