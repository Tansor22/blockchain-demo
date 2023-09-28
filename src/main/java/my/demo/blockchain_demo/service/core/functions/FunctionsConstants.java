package my.demo.blockchain_demo.service.core.functions;

import lombok.experimental.UtilityClass;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;

@UtilityClass
public class FunctionsConstants {
    // makeTrade(bytes32 _currency, uint256 _amount, uint256 _orderId, uint256 _code, uint256 deadline)
    public static final Event MAKE_TRADE = new Event("makeTrade", List.of(
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
    public static final Event MAKE_PAYOUT = new Event("makePayout", List.of(
            new TypeReference<Address>() {
            },
            new TypeReference<Bytes32>() {
            },
            new TypeReference<Uint256>() {
            },
            new TypeReference<Bytes32>() {
            }
    ));
}
