package my.demo.blockchain_demo.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.contract.functions.GetProposedCurrencies;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.core.scenarios.shared.Scenario;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "proposedCurrencies")
public class ProposedCurrenciesScenario extends OracleCall implements Scenario {

    protected ProposedCurrenciesScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder encoder, FunctionParser parser, FunctionConverter converter) {
        super(rpcClient, appConfiguration, encoder, parser, converter);
    }

    void debug(String result, GetProposedCurrencies func) {
        var proposedCurrencies = parser.parseFunctionReturnAsStrings(result, func);
        log.trace("Proposed currencies: {}", proposedCurrencies);
    }
    @Override
    public void go() throws Exception {
        var func = new GetProposedCurrencies();
        var result = callView(func);

        debug(result, func);
    }

}
