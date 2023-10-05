package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.contract.functions.GetProposedCurrencies;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "proposedCurrencies")
public class ProposedCurrenciesScenario extends OracleCall {
    protected ProposedCurrenciesScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    void debug(String result, GetProposedCurrencies func) {
        var proposedCurrencies = onChainDataParser.parseFunctionReturnAsStrings(result, func);
        log.trace("Proposed currencies: {}", proposedCurrencies);
    }
    @Override
    public void go() throws Exception {
        var func = new GetProposedCurrencies();
        var data = onChainEncoder.encodeFunction(func);
        var result = callView(data);

        debug(result, func);
    }

}
