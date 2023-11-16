package my.demo.blockchain_demo.core.contract.functions;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.util.List;

public class GetApprovedCurrencies implements DeFiFunction{
    @Override
    public String name() {
        return "getApprovedCurrencyList";
    }

    @Override
    public List output() {
        return List.of(
                new TypeReference<DynamicArray<Bytes32>>() {}
        );
    }
}
