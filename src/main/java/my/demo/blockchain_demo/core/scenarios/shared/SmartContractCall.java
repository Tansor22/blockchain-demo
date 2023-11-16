package my.demo.blockchain_demo.core.scenarios.shared;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.contract.events.DeFiEvent;
import my.demo.blockchain_demo.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import org.apache.commons.lang3.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

@RequiredArgsConstructor
@Slf4j
public class SmartContractCall {
    protected final EthJsonRpcExt rpcClient;
    protected final AppConfiguration appConfiguration;
    protected final FunctionEncoder encoder;
    protected final FunctionParser parser;
    protected final FunctionConverter converter;

    protected void parseLogs(TransactionReceipt txReceipt, DeFiEvent deFiEvent) {
        for (int i = 0; i < txReceipt.getLogs().size(); i++) {
            var l = txReceipt.getLogs().get(i);
            if (l.isRemoved()) {
                log.trace("Log '{}' was reverted by chain reorganization", i);
            }
            if (StringUtils.isNotEmpty(l.getData())) {
                var logParams = parser.parseEventLog(l, deFiEvent);
                if (logParams.isEmpty()) {
                    continue;
                }
                log.trace("Log '{}' corresponds to event {}, params: {}", i, deFiEvent.name(), logParams);

            }
        }
        // topics empty
    }

    protected TransactionReceipt waitForReceipt(String txHash) throws IOException {
        return waitForReceipt(txHash, 1_000, 5);
    }

    protected TransactionReceipt waitForReceipt(String txHash, long timeout, long attempts) throws IOException {
        TransactionReceipt receipt;
        var attempt = 0;
        do {
            try {
                log.trace("Wait for receipt ... attempt {}", attempt++);
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupt occurred", e);
            }
        } while ((receipt = rpcClient.getTransactionReceipt(txHash)) == null && attempt <= attempts);
        if (receipt == null) {
            log.error("Couldn't get receipt for {} attempts with timeout {} for tx: {}", attempts, timeout, txHash);
        }
        return receipt;
    }

    protected String callView(DeFiFunction func) throws IOException {
        var oracleAddress = appConfiguration.oracleAddress();
        var smartContractAddress = appConfiguration.walletContractAddress();
        var data = encoder.encodeFunction(func);
        return rpcClient.call(oracleAddress, smartContractAddress, data);
    }

    protected String submitTransaction(DeFiFunction func, String from, String fromPrivateKey) throws Exception {
        var balance = rpcClient.getBalance(from);
        if (balance.compareTo(BigInteger.ZERO) == 0) {
            log.trace("Zero balance at {}", from);
            throw new IllegalAccessException("Couldn't submit transaction.");
        }
        log.trace("Balance at {}: {} ", from, balance);

        var nonce = rpcClient.getNonce(from);
        log.trace("Nonce for {}: {}", from, nonce);
        var tx = converter.convert(func, nonce);

        var credentials = Credentials.create(fromPrivateKey);
        var signedMessage = TransactionEncoder.signMessage(tx, appConfiguration.chainId(), credentials);
        var hexValue = Numeric.toHexString(signedMessage);
        var hash = rpcClient.sendTransaction(hexValue);
        log.trace("Tx {} submitted", hash);
        return hash;
    }
}
