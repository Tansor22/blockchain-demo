package my.demo.blockchain_demo.service.core.contract;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.core.domain.MakePayout;
import my.demo.blockchain_demo.service.core.domain.MakeTrade;
import my.demo.blockchain_demo.service.core.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OnChainDataParser {
    private final Map<String, Integer> CURRENCIES_BY_PRECISION =
            // todo precision for matic ??
            Map.of("usdt", 6, "matic", 18);


    private List<Type> parseEventParams(String inputData, Event event) {
        if (inputData.length() < 10) {
            return Collections.emptyList();
        }
        var methodId = inputData.substring(0, 10);
        var methodSignature = EventEncoder.encode(event);
        if (!methodId.equals(methodSignature.substring(0, 10))) {
            return Collections.emptyList();
        }
        return FunctionReturnDecoder.decode(inputData.substring(10), event.getParameters());
    }

    private @Nullable String parseString(Type abiType) {
        if (abiType instanceof Bytes32 bytes32) {
            var rawBytes = bytes32.getValue();
            var bytes = CommonUtils.removeTrailingZeros(rawBytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } else if (abiType instanceof Uint256 uint256) {
            return uint256.getValue().toString();
        } else if (abiType instanceof Address address) {
            return address.getValue();
        } else {
            throw new IllegalArgumentException(
                    String.format("Incorrect type for input %s, should be bytes32.", abiType.getTypeAsString()));
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

    public List<String> parseApprovedCurrencies(String data) {
        var output = parseFunctionReturn(data, Constants.GET_APPROVED_CURRENCY_LIST);
        return parseStringArray(output.get(0));
    }

    public List<String> parseProposedCurrencies(String data) {
        var output = parseFunctionReturn(data, Constants.GET_PROPOSED_CURRENCY_LIST);
        return parseStringArray(output.get(0));
    }

    public Map<String, ?> parseFunctionReturnAsMap(String data, Function func) {
        var types =  parseFunctionReturn(data, func);
        if (types == null) {
            return Map.of();
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

    private List<Type> parseFunctionReturn(String data, Function func) {
        return FunctionReturnDecoder.decode(data, func.getOutputParameters());
    }

    public MakeTrade parseMakeTrade(String data) {
        var params = parseEventParams(data, Constants.MAKE_TRADE);

        var currency = parseString(params.get(0));
        var amount = parseAmount(params.get(1), currency);
        var orderId = parseString(params.get(2));
        var code = parseString(params.get(3));
        var deadline = parseString(params.get(4));

        return new MakeTrade(currency, amount, orderId, code, deadline);
    }

    public MakePayout parseMakePayout(String data) {
        var params = parseEventParams(data, Constants.MAKE_PAYOUT);

        var recipient = parseString(params.get(0));

        var currency = parseString(params.get(1));
        var amount = parseAmount(params.get(2), currency);
        var uniqueId = parseString(params.get(3));

        return new MakePayout(recipient, currency, amount, uniqueId);
    }
}
