package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.FunctionConverter;
import my.demo.blockchain_demo.service.core.contract.FunctionParser;
import my.demo.blockchain_demo.service.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.service.core.contract.functions.MakePayoutFunction;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "makePayout")
public class MakePayoutScenario extends OracleCall {
    protected MakePayoutScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder functionEncoder, FunctionParser functionParser, FunctionConverter converter, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, functionEncoder, functionParser, converter, shutdownManager);
    }

    void debug(String txHash, MakePayoutFunction func) throws Exception {
        var tx = rpcClient.getTransaction(txHash);
        var txReceipt = rpcClient.getTransactionReceipt(txHash);
        log.trace("Tx status: {}", txReceipt.getStatus());
        var data = tx.getInput();
        var inputParams = parser.parseInputParamsAsStrings(data, func);
        log.trace("Params: {}", inputParams);
    }

    @Override
    public void go() throws Exception {
        // dummy yet
        var address = "";
        var currency = "";
        var amount = BigInteger.ONE;
        var uniqueId = "0";
        // create tx
        var func = new MakePayoutFunction(address, currency, amount, uniqueId);
        var txHash = submitTransaction(func);
        debug(txHash, func);
    }
}
