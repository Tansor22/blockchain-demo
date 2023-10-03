package my.demo.blockchain_demo.service.core.contract;

import lombok.experimental.UtilityClass;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;

@UtilityClass
// solidity types mapping - https://github.com/web3j/web3j/blob/master/codegen/src/test/java/org/web3j/codegen/SolidityFunctionWrapperTest.java
public class Constants {
    // getApprovedCurrencyList() external view returns (bytes32[] memory)
    public static final Function GET_APPROVED_CURRENCY_LIST = new Function("getApprovedCurrencyList",
            List.of(),
            List.of(new TypeReference<DynamicArray<Bytes32>>() {
    }));
    // getProposedCurrencyList() external view returns (bytes32[] memory)
    public static final Function GET_PROPOSED_CURRENCY_LIST = new Function("getProposedCurrencyList",
            List.of(),
            List.of(new TypeReference<DynamicArray<Bytes32>>() {
    }));

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
