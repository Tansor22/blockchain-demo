package my.demo.blockchain_demo.service.core.scenarios.shared;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SmartContractCall {
    protected final EthJsonRpcExt rpcClient;
    protected final AppConfiguration appConfiguration;
    protected final OnChainEncoder onChainEncoder;
    protected final OnChainDataParser onChainDataParser;
    protected final ApplicationShutdownManager shutdownManager;

    // max gas to be burnt by transaction submitting, depends on size of input data
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(100_000);
    public static final BigInteger MAX_PRIORITY_FEE_PER_GAS = BigInteger.valueOf(1_500_000_000);


    private BigInteger getMaxFeePerGas() throws IOException {
        var block = rpcClient.getBlockByNumber();
        var baseFeePerGas = block.getBaseFeePerGas();
        return baseFeePerGas.multiply(BigInteger.TWO).add(MAX_PRIORITY_FEE_PER_GAS);
    }

    protected String submitTransaction(long chainId, String data, String from, String fromPrivateKey) throws Exception {
        var balance = rpcClient.getBalance(from);
        if (balance.compareTo(BigInteger.ZERO) == 0) {
            log.trace("Zero balance at {}", from);
            shutdownManager.initiateShutdown();
            throw new IllegalAccessException("Couldn't submit transaction.");
        }
        log.trace("Balance at {}: {} ", from, balance);

        var nonce = rpcClient.getNonce(from);
        log.trace("Nonce for {}: {}", from, nonce);
        // max priority fee per gas, max fee per gas
        var tx =
                RawTransaction.createTransaction(chainId, nonce, GAS_LIMIT, appConfiguration.smartContractAddress(), BigInteger.ZERO, data,
                        MAX_PRIORITY_FEE_PER_GAS, getMaxFeePerGas());

        var credentials = Credentials.create(fromPrivateKey);
        var signedMessage = TransactionEncoder.signMessage(tx, chainId, credentials);
        var hexValue = Numeric.toHexString(signedMessage);
        var hash = rpcClient.sendTransaction(hexValue);
        log.trace("Tx {} submitted", hash);
        return hash;
    }
}
