package my.demo.blockchain_demo.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.contract.events.Trade;
import my.demo.blockchain_demo.core.contract.functions.MakeTradeFunction;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.core.scenarios.shared.Scenario;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "makeTrade")
public class MakeTradeScenario extends OracleCall implements Scenario {

    protected MakeTradeScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder encoder, FunctionParser parser, FunctionConverter converter) {
        super(rpcClient, appConfiguration, encoder, parser, converter);
    }

    @Override
    public void go() throws Exception {
        // dummy yet
        var coin = appConfiguration.coin("matic");
        var amount = coin.amount(.04);
        var orderId = BigInteger.valueOf(112);
        var code = BigInteger.valueOf(1111);
        var deadline = BigInteger.ZERO;
        // create on chain
        var function = new MakeTradeFunction(coin.name(), amount, orderId, code, deadline);
        var txHash = submitTransaction(function);
        //var txHash = "0xa335a2e890ef13c1ded61dfc1da4cff772a6b57dc166dc47f24b3fc64b3900e9";
        debug(txHash, function);
    }

    private void debug(String txHash, MakeTradeFunction func) throws Exception {
        // retrieve data
        var tx = rpcClient.getTransaction(txHash);
        var txReceipt = rpcClient.getTransactionReceipt(txHash);
        if (txReceipt != null) {
            log.trace("Tx status: {}", txReceipt.getStatus());
            parseLogs(txReceipt, new Trade());
        } else {
            log.error("No transaction receipt!");
        }
        var data = tx.getInput();
        var parsed = parser.parseInputParamsAsStrings(data, func);
        log.trace("Submitted params: {}", parsed);
       
    }
}
