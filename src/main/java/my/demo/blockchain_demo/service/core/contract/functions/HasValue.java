package my.demo.blockchain_demo.service.core.contract.functions;

import java.math.BigInteger;

public interface HasValue {

    default BigInteger value() {
        return BigInteger.ONE;
    }
}
