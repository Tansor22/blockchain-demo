package my.demo.blockchain_demo.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.contract.functions.GetProposedCurrency;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.core.scenarios.shared.Scenario;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "proposedCurrency")
public class ProposedCurrencyScenario extends OracleCall implements Scenario {


    protected ProposedCurrencyScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder encoder, FunctionParser parser, FunctionConverter converter) {
        super(rpcClient, appConfiguration, encoder, parser, converter);
    }

    void debug(String result, GetProposedCurrency func) {
        var currency = parser.parseFunctionReturnAsStrings(result, func);
        log.trace("Currency: {}", currency);
    }

    @Override
    public void go() throws Exception {
        var currency = "matic";
        var func = new GetProposedCurrency(currency);
        var result = callView(func);
        debug(result, func);
    }
}
