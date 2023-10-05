package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.contract.functions.ProposeCurrencyFunction;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.TreasuryCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "proposeCurrency")
public class ProposeCurrencyScenario extends TreasuryCall {
    protected ProposeCurrencyScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    private void debug(String hash, ProposeCurrencyFunction func) throws IOException {
        var tx = rpcClient.getTransaction(hash);
        var txReceipt = rpcClient.getTransactionReceipt(hash);

        if (txReceipt != null) {
            log.trace("Tx status: {}", txReceipt.getStatus());
        } else  {
            log.trace("No receipt! Failed transaction {}", hash);
        }

        var params = onChainDataParser.parseInputParamsAsStrings(tx.getInput(), func);
        log.trace("Parameters: {}", params);

    }

    @Override
    public void go() throws Exception {

        var currency = "matic";
        var contractAddress = appConfiguration.smartContractAddress();
        var min = 1L;
        var max = 1_000L;

        var func = new ProposeCurrencyFunction(currency, contractAddress, min, max);
        var data = onChainEncoder.encodeFunction(func);
        var txHash = submitTransaction(0, data);
        log.trace("Tx hash: {}", txHash);

        debug(txHash, func);
    }
}
