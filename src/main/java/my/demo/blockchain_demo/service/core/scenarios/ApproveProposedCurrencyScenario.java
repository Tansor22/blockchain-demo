package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.FunctionConverter;
import my.demo.blockchain_demo.service.core.contract.FunctionParser;
import my.demo.blockchain_demo.service.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.service.core.contract.functions.ApproveProposedCurrency;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.TreasuryCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "approveProposedCurrency")
public class ApproveProposedCurrencyScenario extends TreasuryCall {

    public ApproveProposedCurrencyScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder functionEncoder, FunctionParser functionParser, FunctionConverter converter, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, functionEncoder, functionParser, converter, shutdownManager);
    }

    @Override
    public void go() throws Exception {
        var currency = "matic";
        var contractAddress = appConfiguration.smartContractAddress();
        var min = BigInteger.valueOf(1L);
        var max = BigInteger.valueOf(1_000L);

        var func = new ApproveProposedCurrency(currency, contractAddress, min, max);
        var txHash = submitTransaction(func, 1);
        debug(txHash, func);
    }

    private void debug(String txHash, ApproveProposedCurrency func) throws Exception {
        var tx = rpcClient.getTransaction(txHash);
        var txReceipt = rpcClient.getTransactionReceipt(txHash);

        if (txReceipt != null) {
            log.trace("Tx status: {}", txReceipt.getStatus());
        } else  {
            log.trace("No receipt! Failed transaction {}", txHash);
        }

        var params = parser.parseInputParamsAsStrings(tx.getInput(), func);
        log.trace("Parameters: {}", params);
    }
}
