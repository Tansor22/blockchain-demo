package my.demo.blockchain_demo.service.core.contract.functions;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;

import java.util.List;

// solidity types mapping - https://github.com/web3j/web3j/blob/master/codegen/src/test/java/org/web3j/codegen/SolidityFunctionWrapperTest.java
public interface DeFiFunction extends HasValue {
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
