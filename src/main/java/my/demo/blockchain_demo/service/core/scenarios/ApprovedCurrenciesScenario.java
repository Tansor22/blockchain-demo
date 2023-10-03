package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "approvedCurrencies")
public class ApprovedCurrenciesScenario extends OracleCall<List<String>> {
    public ApprovedCurrenciesScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    @Override
    public List<String> go() throws Exception {
        var data = onChainEncoder.encodeGetApprovedCurrencies();
        var result = callView(data);

        var approvedCurrencies =  onChainDataParser.parseApprovedCurrencies(result);
        log.trace("Approved currencies: {}", approvedCurrencies);
        return approvedCurrencies;
    }
}
