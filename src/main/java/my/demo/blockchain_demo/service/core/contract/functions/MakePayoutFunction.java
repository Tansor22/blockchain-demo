package my.demo.blockchain_demo.service.core.contract.functions;

import lombok.AllArgsConstructor;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;

@AllArgsConstructor
public class MakePayoutFunction implements DeFiFunction {
    private final String recipient;
    private final String currency;
    private final long amount;
    private final String uniqueId;

    // makePayout(address  _recipient, bytes32 _currency, uint256 _amount, bytes32 _uniqueId)
    @Override
    public String name() {
        return "makePayout";
    }

    @Override
    public List<Object> inputParams() {
        return List.of(recipient, currency, amount, uniqueId);
    }

    @Override
    public List<Class<? extends Type>> input() {
        return List.of(Address.class, Bytes32.class, Uint256.class, Bytes32.class);
    }

}
