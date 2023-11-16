package my.demo.blockchain_demo.core.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import org.springframework.stereotype.Service;
import org.web3j.crypto.RawTransaction;

import java.io.IOException;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Slf4j
// converts defi functions to ethereum transactions
public class FunctionConverter {
    private final AppConfiguration config;

    protected final EthJsonRpcExt rpcClient;
    protected final FunctionEncoder encoder;

    // max gas to be burnt by transaction submitting, depends on size of input data
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(100_000);
    public static final BigInteger MAX_PRIORITY_FEE_PER_GAS = BigInteger.valueOf(1_500_000_000);

    public RawTransaction convert(DeFiFunction func, BigInteger nonce) throws IOException {
        return convert(encoder.encodeFunction(func), BigInteger.ZERO, nonce);
    }

    public RawTransaction convert(String data, BigInteger value, BigInteger nonce) throws IOException {
        var chainId = config.chainId();
        return RawTransaction.createTransaction(chainId, nonce, GAS_LIMIT, config.walletContractAddress(),
                value, data, MAX_PRIORITY_FEE_PER_GAS, getMaxFeePerGas());
    }

    private BigInteger getMaxFeePerGas() throws IOException {
        var block = rpcClient.getBlockByNumber();
        var baseFeePerGas = block.getBaseFeePerGas();
        return baseFeePerGas.multiply(BigInteger.TWO).add(MAX_PRIORITY_FEE_PER_GAS);
    }
}
