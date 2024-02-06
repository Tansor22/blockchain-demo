package my.demo.blockchain_demo.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.core.contract.functions.GetApprovedCurrency;
import my.demo.blockchain_demo.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.core.scenarios.shared.Scenario;
import my.demo.blockchain_demo.core.wallet.Wallet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "approvedCurrency")
public class ApprovedCurrencyScenario extends OracleCall implements Scenario {
    private final Wallet readOnlyWallet;

    protected ApprovedCurrencyScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder encoder, FunctionParser parser, FunctionConverter converter, Wallet readOnlyWallet) {
        super(rpcClient, appConfiguration, encoder, parser, converter);
        this.readOnlyWallet = readOnlyWallet;
    }


    @Override
    public void go() throws Exception {
        var currency = "matic";
        var func = new GetApprovedCurrency(currency);

        var result = callView(func);
        var approvedCurrency = parser.parseFunctionReturnAsStrings(result, func);
        log.trace("Reinventing the wheel: Approved currency: {}", approvedCurrency);

        var approvedCurrency2 = readOnlyWallet.getApprovedCurrency(currency);

        log.trace("Lib: Approved currency: {}", approvedCurrency2);

    }
}
