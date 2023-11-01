package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.FunctionConverter;
import my.demo.blockchain_demo.service.core.contract.FunctionParser;
import my.demo.blockchain_demo.service.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.service.core.contract.events.DeFiEvent;
import my.demo.blockchain_demo.service.core.contract.events.Payout;
import my.demo.blockchain_demo.service.core.contract.functions.MakePayoutFunction;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.OracleCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
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
        //var address = "0000000000000000000000000000000000000001";
        var coin = appConfiguration.coin("matic");
        var currency = coin.name();
        var amount = 0.05;
        var uniqueId = "1234567";
        // create tx
        var func = new MakePayoutFunction(address, currency, coin.amount(amount), uniqueId);
        var txHash = submitTransaction(func);


        //log.trace("Hex {}:", new String(Hex.encode("123456".getBytes())));
        // 0x002c015a000000000000000000000000dfe5e5ebe1281e2601f7255144b8b9da8911b4257573647400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001f58595a0000000000000000000000000000000000000000000000000000000000
        //debug(txHash, func);
       /* var encoded = encoder.encodeFunction(func);
        log.trace("Encoded: {}", encoded);
        */
        var my2 = "0x002c015a000000000000000000000000dfe5e5ebe1281e2601f7255144b8b9da8911b4256d61746963000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005c71d3c089740a6a8ab2c000000003132333435000000000000000000000000000000000000000000000000000000";
    var data = "0x002c015a000000000000000000000000dfe5e5ebe1281e2601f7255144b8b9da8911b4256d61746963000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000006a94d74f4300003132333435360000000000000000000000000000000000000000000000000000";
        //var inputParams = parser.parseInputParamsAsStrings(data, func);
        //log.trace("Params: {}", inputParams);
        //submitTransaction(data, BigInteger.ZERO, appConfiguration.oracleAddress(), appConfiguration.oraclePrivateKey());
        //var my = parser.parseInputParamsAsStrings(data, func);
        //log.trace("My Params: {}", my);
    }
}
