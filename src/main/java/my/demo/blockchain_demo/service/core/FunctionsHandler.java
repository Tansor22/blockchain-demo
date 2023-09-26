package my.demo.blockchain_demo.service.core;

import my.demo.blockchain_demo.service.core.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.Collections;
import java.util.List;

@Service
// keep it simple so far, can be make abstract and have hierarchy to handle specific functions
public class FunctionsHandler {
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
}
