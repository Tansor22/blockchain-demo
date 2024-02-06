package my.demo.blockchain_demo.core.contract.functions;

import lombok.AllArgsConstructor;
import my.demo.blockchain_demo.core.wallet.entity.Currency;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.util.List;

@AllArgsConstructor
public class GetApprovedCurrency implements DeFiFunction{
    private final String currency;

    // getApprovedCurrency(bytes32 _name) external view returns (CurrencyLib.Currency memory)
    @Override
    public String name() {
        return "getApprovedCurrency";
    }

    @Override
    public List<Object> inputParams() {
        return List.of(currency);
    }

    @Override
    public List<Class<? extends Type>> input() {
        return List.of(Bytes32.class);
    }

    @Override
    public List output() {
        return List.of(
                new TypeReference<Currency>() {
                }
        );
    }
}
