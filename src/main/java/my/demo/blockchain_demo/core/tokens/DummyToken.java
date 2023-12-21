package my.demo.blockchain_demo.core.tokens;

import my.demo.blockchain_demo.core.contract.ContractEx;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

// simple readonly
public class DummyToken extends ContractEx {

    public DummyToken(Web3j web3j, String contractAddress, String fromAddress) {
        super(web3j, contractAddress, fromAddress);
    }

    public BigInteger decimals() {
        return noParamsSingleValueReturn("decimals", Uint.class).getValue();
    }

    public String name() {
        return noParamsSingleValueReturn("name", Utf8String.class).getValue();
    }

    public String symbol() {
        return noParamsSingleValueReturn("symbol", Utf8String.class).getValue();
    }

    public BigInteger totalSupply() {
        return noParamsSingleValueReturn("totalSupply", Uint256.class).getValue();
    }


}
