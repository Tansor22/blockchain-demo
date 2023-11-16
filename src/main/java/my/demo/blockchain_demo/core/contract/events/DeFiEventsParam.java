package my.demo.blockchain_demo.core.contract.events;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;

public record DeFiEventsParam(
        int index, String name, TypeReference<? extends Type> type
) {
}
