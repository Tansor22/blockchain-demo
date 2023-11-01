package my.demo.blockchain_demo.service.core.contract.events;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;

// event Payout(address indexed recipient, bytes32 currency, uint256 amount, bytes32 indexed uniqueId);
public class Payout implements DeFiEvent{
    @Override
    public String name() {
        return "Payout";
    }

    @Override
    public List<DeFiEventsParam> params() {
        return List.of(
                new DeFiEventsParam(0, "recepient", TypeReference.create(Address.class, true)),
                new DeFiEventsParam(1, "currency", TypeReference.create(Bytes32.class, false)),
                new DeFiEventsParam(2, "amount", TypeReference.create(Uint256.class, false)),
                new DeFiEventsParam(3, "uniqueId", TypeReference.create(Bytes32.class, true))
        );
    }
}
