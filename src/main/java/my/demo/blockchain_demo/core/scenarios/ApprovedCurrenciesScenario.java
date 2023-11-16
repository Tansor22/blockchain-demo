package my.demo.blockchain_demo.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.core.wallet.Wallet;
import my.demo.blockchain_demo.core.contract.functions.GetApprovedCurrencies;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "approvedCurrencies")
public class ApprovedCurrenciesScenario extends OracleCall {

    protected ApprovedCurrenciesScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder encoder, FunctionParser parser, FunctionConverter converter) {
        super(rpcClient, appConfiguration, encoder, parser, converter);
    }

    void debug(String result, GetApprovedCurrencies func) {
        var approvedCurrencies = parser.parseFunctionReturnAsStrings(result, func);
        log.trace("Approved currencies: {}", approvedCurrencies);
    }

    @Override
    public void go() throws Exception {
        var wallet = new Wallet(rpcClient, appConfiguration.walletContractAddress(), appConfiguration.oracleAddress());
        var approvedCurrencies = wallet.getApprovedCurrencies();
        log.trace("Approved currencies: {}", approvedCurrencies);
    }
}
