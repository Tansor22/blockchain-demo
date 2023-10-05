package my.demo.blockchain_demo.service.core.contract;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.service.core.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OnChainEncoder {
    public Function buildFunction(DeFiFunction deFiFunction) {
        // fight against type erasure
        List inputParams = buildInputParams(deFiFunction.input(), deFiFunction.inputParams());
        List outputParams = deFiFunction.output();
        return new Function(deFiFunction.name(), inputParams, outputParams);
    }

    public String encodeFunction(DeFiFunction deFiFunction) {
        var function = buildFunction(deFiFunction);
        return FunctionEncoder.encode(function);
    }

    private List<? extends Type> buildInputParams(List<Class<? extends Type>> typeClasses, List<Object> params) {
        Preconditions.checkArgument(typeClasses.size() >= params.size(), "Size of expected params should be greater or equal.");

        var inputParams = new ArrayList<Type>();
        for (int i = 0; i < typeClasses.size(); i++) {
            try {
                var type = abiTypeInstance(typeClasses.get(i), params.get(i));
                inputParams.add(type);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(String.format("Problems with matching types of params with index %s. Root cause: %s",
                        i, ex.getMessage()), ex);
            }
        }
        return inputParams;
    }

    private <T> Type abiTypeInstance(Class<? extends Type> typeClass, T value) {
        var typeReference = TypeReference.create(typeClass);
        var fullAbiTypeName = typeReference.getType().getTypeName();
        var simpleAbiTypeName = CommonUtils.cutToLast(fullAbiTypeName, '.');

        var abiTypeInstance = switch (simpleAbiTypeName) {
            case "Bytes32" -> bytes32(value);
            case "Uint256" -> uint256(value);
            case "Address" -> address((String) value);
            case "Uint" -> uint((Long) value);
            default -> throw new UnsupportedOperationException("Unsupported abi type: " + fullAbiTypeName);
        };

        if (abiTypeInstance == null) {
            throw new IllegalArgumentException(String.format("Not corresponded type %s for %s", value.getClass(), fullAbiTypeName));
        }
        return abiTypeInstance;
    }

    private <T> Type uint256(T value) {
        if (value instanceof Long longValue) {
            return new Uint256(longValue);
        } else if (value instanceof Integer intValue) {
            return new Uint256(intValue);
        } else {
            return null;
        }
    }

    private Type address(String value) {
        return new Address(value);
    }

    private Type uint(long value) {
        return new Uint(BigInteger.valueOf(value));
    }

    private <T> Type bytes32(T value) {
        if (value instanceof String stringValue) {
            var stringBytes = stringValue.getBytes();
            var paddedBytes = CommonUtils.padBytesWithZeros(stringBytes, 32);
            return new Bytes32(paddedBytes);
        } else if (value instanceof byte[] bytesValue) {
            return new Bytes32(bytesValue);
        } else {
            return null;
        }
    }

}
