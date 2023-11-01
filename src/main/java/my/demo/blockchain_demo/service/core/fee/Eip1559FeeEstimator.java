package my.demo.blockchain_demo.service.core.fee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.core.fee.entity.FeeData;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;

@RequiredArgsConstructor
@Slf4j
@Service
public class Eip1559FeeEstimator {
    public static final BigInteger MAX_PRIORITY_FEE_PER_GAS = BigInteger.valueOf(1_500_000_000);   // 1.5 GWei

    private final EthJsonRpcExt rpc;

    public FeeData getFeeData() throws IOException {
        var baseFeePerGas = rpc.getPendingBlock().getBaseFeePerGas();
        var maxFeePerGas = baseFeePerGas.multiply(BigInteger.TWO).add(MAX_PRIORITY_FEE_PER_GAS);
        return new FeeData(baseFeePerGas, MAX_PRIORITY_FEE_PER_GAS, maxFeePerGas);
    }
}
