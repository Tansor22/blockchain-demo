package my.demo.blockchain_demo.service.core.contract.events;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;

// event Trade(address indexed recipient, bytes32 currency, uint256 amount, uint256 indexed orderId, uint256 indexed code)
public class Trade implements DeFiEvent{
    @Override
    public String name() {
        return "Trade";
    }

    @Override
    public List<DeFiEventsParam> params() {
        return List.of(
                new DeFiEventsParam(0, "recepient", TypeReference.create(Address.class, true)),
                new DeFiEventsParam(1, "currency", TypeReference.create(Bytes32.class, false)),
                new DeFiEventsParam(2, "amount", TypeReference.create(Uint256.class, false)),
                new DeFiEventsParam(3, "orderId", TypeReference.create(Uint256.class, true)),
                new DeFiEventsParam(4, "code", TypeReference.create(Uint256.class, true))
        );
    }
}
