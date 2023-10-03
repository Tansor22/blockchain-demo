package my.demo.blockchain_demo.service.core.contract;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.core.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OnChainEncoder {
    // supported package
    private static final String ABI_DATA_TYPES_PACKAGE = "org.web3j.abi.datatypes.generated";

    public String encodeMakeTrade(String currency, long amount, long orderId, long code){
        return encodeEventWithParams(Constants.MAKE_TRADE, currency, amount, orderId, code, 0);
    }

    public String encodeMakePayout(String address, String currency, long amount, long uniqueId){
        return encodeEventWithParams(Constants.MAKE_TRADE, address, currency, amount, uniqueId);
    }

    public String encodeGetApprovedCurrencies() {
        return encodeFunctionCall(Constants.GET_APPROVED_CURRENCY_LIST);
    }
    private String encodeFunctionCall(Function func) {
        return FunctionEncoder.encode(func);
    }
    private String encodeEventWithParams(Event event, Object... params) {
        var eventParams = event.getParameters();
        Preconditions.checkArgument(eventParams.size() >= params.length, "Size of expected params should be greater or equal.");

        var inputParams = new ArrayList<Type>();
        for (int i = 0; i < eventParams.size(); i++) {
            try {
                var type = abiTypeInstance(eventParams.get(i), params[i]);
                inputParams.add(type);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(String.format("Problems with matching types of params with index %s. Root cause: %s",
                        i, ex.getMessage()), ex);
            }
        }

        // with no output params yet
        var func = new Function(event.getName(), inputParams, List.of());
        return FunctionEncoder.encode(func);
    }

    private <T> Type abiTypeInstance(TypeReference<?> typeReference, T value) {
        var fullAbiTypeName = typeReference.getType().getTypeName();
        Preconditions.checkArgument(fullAbiTypeName.startsWith(ABI_DATA_TYPES_PACKAGE), "Unsupported data type: {}", fullAbiTypeName);
        // package name and 'dot'
        var simpleAbiType = fullAbiTypeName.substring(ABI_DATA_TYPES_PACKAGE.length() + 1);

        var abiTypeInstance = switch (simpleAbiType) {
            case "Bytes32" -> bytes32(value);
            case "Uint256" -> uint256(value);
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
