package my.demo.blockchain_demo.core.contract.functions;

import lombok.AllArgsConstructor;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
public class ApproveProposedCurrency implements DeFiFunction{
    private final String currency;
    private final String contractAddress;
    private final BigInteger min;
    private final BigInteger max;
    // approveProposedCurrency(bytes32 _name, address _contractAddr, uint _minAmount, uint _maxAmount) external onlyTreasurer
    @Override
    public String name() {
        return "approveProposedCurrency";
    }

    @Override
    public List<Object> inputParams() {
        return List.of(currency, contractAddress, min, max);
    }

    @Override
    public List<Class<? extends Type>> input() {
        return List.of(Bytes32.class, Address.class, Uint.class, Uint.class);
    }
}
