package my.demo.blockchain_demo.service.core.contract.functions;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;

import java.util.List;

public interface DeFiFunction {
    String name();

    default List<Object> inputParams() {
        return List.of();
    }
    default List<Class<? extends Type>> input() {
        return List.of();
    }

    default List<TypeReference<Type>> output() {
        return List.of();
    }
}
