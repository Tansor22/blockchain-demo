package my.demo.blockchain_demo.service.core;

import lombok.RequiredArgsConstructor;
import my.demo.blockchain_demo.service.core.domain.MakePayout;
import my.demo.blockchain_demo.service.core.domain.MakeTrade;
import my.demo.blockchain_demo.service.core.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
// keep it simple so far, can be make abstract and have hierarchy to handle specific functions
public class FunctionsHandler {
    private final AmountsParser amountsParser;
    private final StringsParser stringsParser;
    // makeTrade(bytes32 _currency, uint256 _amount, uint256 _orderId, uint256 _code, uint256 deadline)
    private static final Event MAKE_TRADE = new Event("makeTrade", List.of(
            new TypeReference<Bytes32>() {
            },
            new TypeReference<Uint256>() {
            },
            new TypeReference<Uint256>() {
            },
            new TypeReference<Uint256>() {
            },
            new TypeReference<Uint256>() {
            }
    ));
    // makePayout(address  _recipient, bytes32 _currency, uint256 _amount, bytes32 _uniqueId)
    private static final Event MAKE_PAYOUT = new Event("makePayout", List.of(
            new TypeReference<Address>() {
            },
            new TypeReference<Bytes32>() {
            },
            new TypeReference<Uint256>() {
            },
            new TypeReference<Bytes32>() {
            }
    ));

    public String getMakeTradeData(String currency, long amount, long orderId, long code) {
        var currencyBytes = currency.getBytes();
        var paddedBytes = CommonUtils.padBytesWithZeros(currencyBytes, 32);
        var function = new Function(
                MAKE_TRADE.getName(),
                List.of(new Bytes32(paddedBytes),
                        new Uint256(amount),
                        new Uint256(orderId),
                        new Uint256(code),
                        // deadline
                        new Uint256(0)),
                Collections.emptyList());
        return FunctionEncoder.encode(function);
    }

    public String getMakePayoutData(String address, String currency, long amount, long uniqueId) {
        var currencyBytes = currency.getBytes();
        var paddedBytes = CommonUtils.padBytesWithZeros(currencyBytes, 32);
        var function = new Function(
                MAKE_PAYOUT.getName(),
                List.of(new Address(address),
                        new Bytes32(paddedBytes),
                        new Uint256(amount),
                        new Uint256(uniqueId)),
                Collections.emptyList());
        return FunctionEncoder.encode(function);
    }

    private static List<Type> parse(String inputData, Event event) {
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

    public MakeTrade parseMakeTrade(String data) {
        var parsed = parse(data, MAKE_TRADE);

        var currency = stringsParser.parseString(parsed.get(0));
        var amount = amountsParser.parseAmount(parsed.get(1), currency);
        var orderId = ((Uint256) parsed.get(2)).getValue().toString();
        var code = ((Uint256) parsed.get(3)).getValue().toString();
        var deadline = ((Uint256) parsed.get(4)).getValue().toString();

        return new MakeTrade(currency, amount, orderId, code, deadline);
    }

    public MakePayout parseMakePayout(String data) {
        var parsed = parse(data, MAKE_PAYOUT);

        var recipient = ((Address) parsed.get(0)).getValue();

        var currency = stringsParser.parseString(parsed.get(1));
        var amount = amountsParser.parseAmount(parsed.get(2), currency);
        var uniqueId = stringsParser.parseString(parsed.get(3));

        return new MakePayout(recipient, currency, amount, uniqueId);
    }

}
