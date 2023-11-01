package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.FunctionConverter;
import my.demo.blockchain_demo.service.core.contract.FunctionParser;
import my.demo.blockchain_demo.service.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.service.core.contract.events.DeFiEvent;
import my.demo.blockchain_demo.service.core.contract.events.LogFeeTransfer;
import my.demo.blockchain_demo.service.core.contract.events.Trade;
import my.demo.blockchain_demo.service.core.contract.functions.MakeTradeFunction;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;

import java.math.BigInteger;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "makeTrade")
public class MakeTradeScenario extends OracleCall {

    protected MakeTradeScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration,
                                FunctionEncoder functionEncoder, FunctionParser functionParser,
                                ApplicationShutdownManager shutdownManager, FunctionConverter converter) {
        super(rpcClient, appConfiguration, functionEncoder, functionParser, converter, shutdownManager);
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
