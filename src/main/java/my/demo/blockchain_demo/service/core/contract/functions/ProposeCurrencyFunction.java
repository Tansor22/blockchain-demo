package my.demo.blockchain_demo.service.core.contract.functions;

import lombok.AllArgsConstructor;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
public class ProposeCurrencyFunction implements DeFiFunction{
    private String currency;
    private String contractAddress;
    private BigInteger min;
    private BigInteger max;

    // proposeCurrency(bytes32 _name, address _contractAddr, uint _minAmount, uint _maxAmount) external onlyTreasurer
    @Override
    public String name() {
        return "proposeCurrency";
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
