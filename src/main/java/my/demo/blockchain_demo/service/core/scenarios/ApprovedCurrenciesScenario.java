package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.contract.functions.GetApprovedCurrencies;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "approvedCurrencies")
public class ApprovedCurrenciesScenario extends OracleCall {
    public ApprovedCurrenciesScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    void debug(String result, GetApprovedCurrencies func) {
        var approvedCurrencies = onChainDataParser.parseFunctionReturnAsStrings(result, func);
        log.trace("Approved currencies: {}", approvedCurrencies);
    }

    @Override
    public void go() throws Exception {
        var func = new GetApprovedCurrencies();
        var data = onChainEncoder.encodeFunction(func);
        var result = callView(data);
        debug(result, func);
    }
}
