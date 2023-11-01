package my.demo.blockchain_demo.service.core.fee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class FeeData {
    // EIP1559
    private BigInteger baseFeePerGas;
    private BigInteger maxPriorityFeePerGas;
    private BigInteger maxFeePerGas;
}
