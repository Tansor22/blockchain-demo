package my.demo.blockchain_demo.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.core.contract.events.Payout;
import my.demo.blockchain_demo.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.contract.functions.MakePayoutFunction;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "makePayout")
public class MakePayoutScenario extends OracleCall {

    protected MakePayoutScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder encoder, FunctionParser parser, FunctionConverter converter) {
        super(rpcClient, appConfiguration, encoder, parser, converter);
    }

    void debug(String txHash, MakePayoutFunction func) throws Exception {
        var tx = rpcClient.getTransaction(txHash);
        var txReceipt = waitForReceipt(txHash);
        if (txReceipt != null) {
            log.trace("Tx status: {}", txReceipt.getStatus());
            parseLogs(txReceipt, new Payout());
        } else {
            log.error("No tx receipt!");
        }
        var data = tx.getInput();
        var inputParams = parser.parseInputParamsAsStrings(data, func);
        log.trace("Params: {}", inputParams);
    }

    @Override
    public void go() throws Exception {
        var address = appConfiguration.treasuries().get(1).address();
        var coin = appConfiguration.coin("matic");
        var currency = coin.name();
        var amount = 0.05;
        var uniqueId = "1234567";
        // create tx
        var func = new MakePayoutFunction(address, currency, coin.amount(amount), uniqueId);
        var txHash = submitTransaction(func);
        debug(txHash, func);

    }
}
