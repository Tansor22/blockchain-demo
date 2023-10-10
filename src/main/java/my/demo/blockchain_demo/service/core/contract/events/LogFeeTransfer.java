package my.demo.blockchain_demo.service.core.contract.events;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;

public class LogFeeTransfer implements DeFiEvent{
    @Override
    public String name() {
        return "LogFeeTransfer";
    }

    @Override
    public List<DeFiEventsParam> params() {
        return List.of(
                new DeFiEventsParam(0, "token",
                        TypeReference.create(Address.class, true)),
                new DeFiEventsParam(1, "from",
                        TypeReference.create(Address.class, true)),
                new DeFiEventsParam(2, "to",
                        TypeReference.create(Address.class, true)),

                new DeFiEventsParam(3, "amount", TypeReference.create(Uint256.class)),
                new DeFiEventsParam(4, "input1", TypeReference.create(Uint256.class)),
                new DeFiEventsParam(5, "input2", TypeReference.create(Uint256.class)),
                new DeFiEventsParam(6, "output1", TypeReference.create(Uint256.class)),
                new DeFiEventsParam(7, "output2", TypeReference.create(Uint256.class))
        );
    }
}
