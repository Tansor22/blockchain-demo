package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.FunctionConverter;
import my.demo.blockchain_demo.service.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.service.core.contract.FunctionParser;
import my.demo.blockchain_demo.service.core.contract.functions.GetProposedCurrency;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "approvedCurrency")
public class ApprovedCurrencyScenario extends OracleCall {
    protected ApprovedCurrencyScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder functionEncoder, FunctionParser functionParser, FunctionConverter converter, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, functionEncoder, functionParser, converter, shutdownManager);
    }

    @Override
    public void go() throws Exception {
        var currency = "matic";
        var coin = appConfiguration.coin(currency);
        var func = new GetProposedCurrency(currency);

        var result = submitTransaction(func);
        var approvedCurrency = parser.parseFunctionReturnAsStrings(result, func);
        log.trace("Approved currency: {}", approvedCurrency);

    }
}
