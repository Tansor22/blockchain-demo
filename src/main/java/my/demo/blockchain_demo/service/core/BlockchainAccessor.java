package my.demo.blockchain_demo.service.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.domain.MakePayout;
import my.demo.blockchain_demo.service.core.domain.MakeTrade;
import my.demo.blockchain_demo.service.core.functions.FunctionsEncoder;
import my.demo.blockchain_demo.service.core.functions.FunctionsParser;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockchainAccessor {
    private final EthJsonRpcExt rpcClient;
    private final AppConfiguration appConfiguration;
    private final FunctionsEncoder functionsEncoder;
    private final FunctionsParser functionsParser;
    private final ApplicationShutdownManager shutdownManager;

    // max gas to be burnt by transaction submitting, depends on size of input data
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(100_000);
    public static final BigInteger MAX_PRIORITY_FEE_PER_GAS = BigInteger.valueOf(1_500_000_000);

    private BigInteger getMaxFeePerGas() throws IOException {
        var block = rpcClient.getBlockByNumber();
        var baseFeePerGas = block.getBaseFeePerGas();
        return baseFeePerGas.multiply(BigInteger.TWO).add(MAX_PRIORITY_FEE_PER_GAS);
    }

    public MakeTrade getMakeTradeData(String txHash) throws Exception {
        var tx = rpcClient.getTransaction(txHash);
        var txReceipt = rpcClient.getTransactionReceipt(txHash);
        log.trace("Tx status: {}", txReceipt.getStatus());
        var data = tx.getInput();
        var makeTradeData = functionsParser.parseMakeTrade(data);
        log.trace("Data of {} parsed: {}",txHash, makeTradeData);
        return makeTradeData;
    }

    public MakePayout getMakePayoutData(String txHash) throws Exception {
        var tx = rpcClient.getTransaction(txHash);
        var data = tx.getInput();
        var makeTradeData = functionsParser.parseMakePayout(data);
        log.trace("Data of {} parsed: {}",txHash, makeTradeData);
        return makeTradeData;
    }

    public String callMakeTrade(String currency, long amount, long orderId, long code) throws Exception {
        var dataEncoded = functionsEncoder.encodeMakeTrade(currency, amount, orderId, code);
        return call(dataEncoded);
    }

    public String callMakePayout(String address, String currency, long amount, long uniqueId) throws Exception {
        var dataEncoded = functionsEncoder.encodeMakePayout(address, currency, amount, uniqueId);
        return call(dataEncoded);
    }

    private String call(String data) throws Exception {
        var chainId = rpcClient.getChainId();
        log.trace("Chain id: {} ", chainId);

        var oracleBalance = rpcClient.getBalance(appConfiguration.oracleAddress());
        if (oracleBalance.compareTo(BigInteger.ZERO) == 0) {
            log.trace("Zero balance at {}", appConfiguration.oracleAddress());
            shutdownManager.initiateShutdown();
            return null;
        }
        log.trace("Balance at {}: {} ", appConfiguration.oracleAddress(), oracleBalance);

        var nonce = rpcClient.getNonce(appConfiguration.oracleAddress());
        log.trace("Nonce for {}: {}", appConfiguration.oracleAddress(), nonce);
        // max priority fee per gas, max fee per gas
        var tx = RawTransaction.createTransaction(chainId, nonce, GAS_LIMIT, appConfiguration.smartContractAddress(),
                BigInteger.ZERO, data, MAX_PRIORITY_FEE_PER_GAS, getMaxFeePerGas());

        var credentials = Credentials.create(appConfiguration.oraclePrivateKey());
        var signedMessage = TransactionEncoder.signMessage(tx, chainId, credentials);
        var hexValue = Numeric.toHexString(signedMessage);
        var hash = rpcClient.sendTransaction(hexValue);
        log.trace("Tx {} submitted", hash);
        return hash;
    }

}
