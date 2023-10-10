package my.demo.blockchain_demo.service.core.contract.functions;

import lombok.AllArgsConstructor;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.List;


@AllArgsConstructor
public class MakeTradeFunction implements DeFiFunction{
    private final String currency;
    private final BigInteger amount;
    private final BigInteger orderId;
    private final BigInteger code;
    private final BigInteger deadline;

    // makeTrade(bytes32 _currency, uint256 _amount, uint256 _orderId, uint256 _code, uint256 deadline)
    @Override
    public String name() {
        return "makeTrade";
    }

    @Override
    public BigInteger value() {
        return amount;
    }

    @Override
    public List<Object> inputParams() {
        return List.of(
                currency,
                amount,
                orderId,
                code,
                deadline
        );
    }

    @Override
    public List<Class<? extends Type>> input() {
        return List.of(
                Bytes32.class,
                Uint256.class,
                Uint256.class,
                Uint256.class,
                Uint256.class
        );
    }
}
