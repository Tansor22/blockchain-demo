package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.contract.functions.ApprovedProposedCurrency;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.TreasuryCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "approveProposedCurrency")
public class ApproveProposedCurrencyScenario extends TreasuryCall {
    protected ApproveProposedCurrencyScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    @Override
    public void go() throws Exception {
        var currency = "matic";
        var contractAddress = appConfiguration.smartContractAddress();
        var min = 1L;
        var max = 1_000L;

        var func = new ApprovedProposedCurrency(currency, contractAddress, min, max);
        var data = onChainEncoder.encodeFunction(func);
        var txHash = submitTransaction(1, data);
        debug(txHash, func);
    }

    private void debug(String txHash, ApprovedProposedCurrency func) throws Exception {
        var tx = rpcClient.getTransaction(txHash);
        var txReceipt = rpcClient.getTransactionReceipt(txHash);

        if (txReceipt != null) {
            log.trace("Tx status: {}", txReceipt.getStatus());
        } else  {
            log.trace("No receipt! Failed transaction {}", txHash);
        }

        var params = onChainDataParser.parseInputParamsAsStrings(tx.getInput(), func);
        log.trace("Parameters: {}", params);
    }
}
