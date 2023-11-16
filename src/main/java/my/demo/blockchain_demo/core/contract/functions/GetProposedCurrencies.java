package my.demo.blockchain_demo.core.contract.functions;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.util.List;

public class GetProposedCurrencies implements DeFiFunction{

    // getProposedCurrencyList() external view returns (bytes32[] memory)
    @Override
    public String name() {
        return "getProposedCurrencyList";
    }

    @Override
    public List output() {
        return List.of(
                new TypeReference<DynamicArray<Bytes32>>() {}
        );
    }
}
