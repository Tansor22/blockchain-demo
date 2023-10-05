package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.contract.functions.MakeTradeFunction;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "makeTrade")
public class MakeTradeScenario extends OracleCall {
    public MakeTradeScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    @Override
    public void go() throws Exception {
        // dummy yet
        var currency = "";
        var amount = 0;
        var orderId = 0;
        var code = 0;
        var deadline = 0;
        // create on chain
        var function = new MakeTradeFunction(currency, amount, orderId, code, deadline);
        var dataEncoded = onChainEncoder.encodeFunction(function);
        var txHash = submitTransaction(dataEncoded);
        debug(txHash, function);
    }
    private void debug(String txHash, MakeTradeFunction func) throws Exception {
        // retrieve data
        var tx = rpcClient.getTransaction(txHash);
        var txReceipt = rpcClient.getTransactionReceipt(txHash);
        log.trace("Tx status: {}", txReceipt.getStatus());
        var data = tx.getInput();
        var parsed = onChainDataParser.parseInputParamsAsStrings(data, func);
        log.trace("Submitted params: {}", parsed);
    }
}
