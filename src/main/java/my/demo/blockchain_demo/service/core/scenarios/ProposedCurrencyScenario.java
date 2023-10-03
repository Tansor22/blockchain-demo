package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "proposedCurrency")
public class ProposedCurrencyScenario extends OracleCall<Map<String, ?>> {

    protected ProposedCurrencyScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    @Override
    public Map<String, ?> go() throws Exception {
        var currency = "matic";
        var func = onChainEncoder.getProposedCurrency(currency);
        var data = onChainEncoder.encodeFunctionCall(func);
        var result = callView(data);
        var parsed = onChainDataParser.parseFunctionReturnAsMap(result, func);
        log.trace("Currency {} : {}", currency, parsed);
        return parsed;
    }
}
