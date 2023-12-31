package my.demo.blockchain_demo.core.contract;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.core.contract.events.DeFiEvent;
import my.demo.blockchain_demo.core.contract.events.DeFiEventsParam;
import my.demo.blockchain_demo.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.core.utils.CommonUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
// low level parser for debug purposes
public class FunctionParser {
    private FunctionEncoder encoder;

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

    public Map<String, String> parseEventLog(Log l, DeFiEvent deFiEvent) {
        var eventValues = Contract.staticExtractEventParameters(deFiEvent.toEvent(), l);
        if (eventValues != null) {
            var output = new HashMap<String, String>();
            var paramsSorted = deFiEvent.params().stream()
                    .sorted(Comparator.comparingInt(DeFiEventsParam::index))
                    .toList();
            int indexed = 0;
            int nonIndexed = 0;
            for (var param : paramsSorted) {
                if (param.type().isIndexed()) {
                    var value = eventValues.getIndexedValues().get(indexed++);
                    output.put(param.name(), parseString(value));
                } else {
                    var value = eventValues.getNonIndexedValues().get(nonIndexed++);
                    output.put(param.name(), parseString(value));
                }
            }
            return output;
        } else {
            return Map.of();
        }
    }

    public Map<String, String> parseEventParams(String inputData, DeFiEvent deFiEvent) {
        if (isInputCorrect(inputData, deFiEvent)) {
            var params = parseFunctionReturn(inputData, deFiEvent);

            var output = new HashMap<String, String>(params.size());
            for (var param : deFiEvent.params()) {
                var index = param.index();
                var type = params.get(index);
                var value = parseString(type);

                output.put(param.name(), value);
            }
            return output;
        } else {
            log.error("Input doesn't correspond to event {}", deFiEvent.name());
            return Map.of();
        }
    }

    private boolean isInputCorrect(String inputData, DeFiFunction deFiFunction) {
        if (inputData.length() < 10) {
            return false;
        }
        var func = encoder.buildFunction(deFiFunction);

        var methodId = inputData.substring(0, 10);

        var methodSignature = org.web3j.abi.FunctionEncoder.encode(func);
        // input corresponds given func
        return methodId.equals(methodSignature.substring(0, 10));
    }

    private boolean isInputCorrect(String inputData, DeFiEvent deFiEvent) {
        if (inputData.length() < 10) {
            return false;
        }
        var methodId = inputData.substring(0, 10);

        var methodSignature = EventEncoder.encode(deFiEvent.toEvent());
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

    private List<Type> parseFunctionReturn(String data, DeFiEvent deFiEvent) {
        List params = deFiEvent.params().stream()
                .sorted(Comparator.comparingInt(DeFiEventsParam::index))
                .map(DeFiEventsParam::type)
                .toList();
        return FunctionReturnDecoder.decode(data, params);
    }

    public List<String> parseFunctionReturnAsStrings(String data, DeFiFunction deFiFunction) {
        var types = parseFunctionReturn(data, deFiFunction);
        return types.stream()
                .map(this::parseString)
                .toList();
    }


    private @Nullable String parseString(Type<?> abiType) {
        if (abiType instanceof Bytes32 bytes32) {
            return CommonUtils.paddedBytes32(bytes32.getValue());
        } else if (abiType instanceof Uint256 uint256) {
            return uint256.getValue().toString();
        } else if (abiType instanceof Uint uint) {
            return uint.getValue().toString();
        } else if (abiType instanceof Address address) {
            return address.getValue();
        } else if (abiType instanceof DynamicArray<? extends Type> array) {
            return String.join(", ", parseStringArray(array));
        } else if (abiType instanceof Bool bool) {
            return BooleanUtils.toStringTrueFalse(bool.getValue());
        } else {
            throw new IllegalArgumentException(
                    String.format("Incorrect type for input %s.", abiType.getTypeAsString()));
        }
    }

    private List<String> parseStringArray(Type<?> abiType) {
        if (abiType instanceof DynamicArray<? extends Type> dynamicArray) {
            return dynamicArray.getValue().stream()
                    .map(this::parseString).toList();
        } else {
            throw new IllegalArgumentException(
                    String.format("Incorrect type for input %s, should be <type>[].", abiType.getTypeAsString()));
        }
    }
}
