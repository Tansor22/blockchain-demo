package my.demo.blockchain_demo.service.core.contract;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.service.core.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class OnChainDataParser {
    private OnChainEncoder encoder;
    private final Map<String, Integer> CURRENCIES_BY_PRECISION =
            // todo precision for matic ??
            Map.of("usdt", 6, "matic", 18);


    /**
     * For debug.
     *
     * @param inputData
     * @param deFiFunction
     * @return
     */

    private List<Type> parseInputParams(String inputData, DeFiFunction deFiFunction) {
        if (isInputCorrect(inputData, deFiFunction)) {
            List typeReferences = deFiFunction.input().stream()
                    .map(TypeReference::create)
                    .toList();
            return FunctionReturnDecoder.decode(inputData.substring(10), typeReferences);
        } else {
            log.error("Input doesn't correspond to func {}", deFiFunction.name());
            return List.of();
        }

    }

    private boolean isInputCorrect(String inputData, DeFiFunction deFiFunction) {
        if (inputData.length() < 10) {
            return false;
        }
        var func = encoder.buildFunction(deFiFunction);

        var methodId = inputData.substring(0, 10);

        var methodSignature = FunctionEncoder.encode(func);
        // input corresponds given func
        return methodId.equals(methodSignature.substring(0, 10));
    }

    public List<String> parseInputParamsAsStrings(String inputData, DeFiFunction deFiFunction) {
        var types = parseInputParams(inputData, deFiFunction);
        return types.stream()
                .map(this::parseString)
                .toList();
    }

    private List<Type> parseFunctionReturn(String data, DeFiFunction deFiFunction) {
        return FunctionReturnDecoder.decode(data, deFiFunction.output());
    }

    public List<String> parseFunctionReturnAsStrings(String data, DeFiFunction deFiFunction) {
        var types = parseFunctionReturn(data, deFiFunction);
        return types.stream()
                .map(this::parseString)
                .toList();
    }


    private @Nullable String parseString(Type abiType) {
        if (abiType instanceof Bytes32 bytes32) {
            var rawBytes = bytes32.getValue();
            var bytes = CommonUtils.removeTrailingZeros(rawBytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } else if (abiType instanceof Uint256 uint256) {
            return uint256.getValue().toString();
        } else if (abiType instanceof Uint uint) {
            return uint.getValue().toString();
        } else if (abiType instanceof Address address) {
            return address.getValue();
        } else if (abiType instanceof DynamicArray array) {
            return String.join(", ", parseStringArray(array));
        } else {
            throw new IllegalArgumentException(
                    String.format("Incorrect type for input %s.", abiType.getTypeAsString()));
        }
    }

    private @Nullable BigDecimal parseAmount(Type abiType, String currency) {
        var precision = CURRENCIES_BY_PRECISION.get(currency.toLowerCase());
        if (precision == null) {
            log.error("Currency {} is not supported.", currency);
        } else {
            if (abiType instanceof Uint256 amount) {
                var rawValue = amount.getValue();
                // should shift by some number
                return new BigDecimal(rawValue).movePointLeft(precision);
            } else {
                throw new IllegalArgumentException(
                        String.format("Incorrect type for input %s, should be uint256.", abiType.getTypeAsString()));
            }
        }
        return null;
    }

    private List<String> parseStringArray(Type abiType) {
        if (abiType instanceof DynamicArray<? extends Type> dynamicArray) {
            return dynamicArray.getValue().stream()
                    .map(this::parseString).toList();
        } else {
            throw new IllegalArgumentException(
                    String.format("Incorrect type for input %s, should be <type>[].", abiType.getTypeAsString()));
        }
    }
}
