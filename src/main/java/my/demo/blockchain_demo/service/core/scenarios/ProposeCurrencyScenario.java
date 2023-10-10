package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.FunctionConverter;
import my.demo.blockchain_demo.service.core.contract.FunctionParser;
import my.demo.blockchain_demo.service.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.service.core.contract.events.LogFeeTransfer;
import my.demo.blockchain_demo.service.core.contract.functions.ProposeCurrencyFunction;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.TreasuryCall;
import my.demo.blockchain_demo.service.core.utils.CommonUtils;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "proposeCurrency")
public class ProposeCurrencyScenario extends TreasuryCall {

    public ProposeCurrencyScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder functionEncoder, FunctionParser functionParser, FunctionConverter converter, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, functionEncoder, functionParser, converter, shutdownManager);
    }

    private void debug(String hash, ProposeCurrencyFunction func) throws IOException {
        var tx = rpcClient.getTransaction(hash);
        var txReceipt = rpcClient.getTransactionReceipt(hash);

        if (txReceipt != null) {
            log.trace("Tx status: {}", txReceipt.getStatus());
        } else  {
            log.trace("No receipt! Failed transaction {}", hash);
        }

        var params = parser.parseInputParamsAsStrings(tx.getInput(), func);
        log.trace("Parameters: {}", params);
        var event = new LogFeeTransfer();
        var eventParams = parser.parseEventParams(txReceipt.getLogs().get(0).getData(), event);
        log.trace("Event params: {}", eventParams);

    }

    @Override
    public void go() throws Exception {

        var currency = "usdt";
        var currencyBytes = CommonUtils.padBytesEncodedWithZeros(currency.getBytes());
        var hex = Hex.encode(currencyBytes);
        // 0x7573647400000000000000000000000000000000000000000000000000000000
        var contractAddress = appConfiguration.smartContractAddress();
        var min = BigInteger.valueOf(10000000000000L);
        var max = BigInteger.valueOf(10000000000000000L);

        var func = new ProposeCurrencyFunction(currency, contractAddress, min, max);
        //var txHash = submitTransaction(func, 0);
        var txHash = "0x845534d3eb6ecdabe34d6dd34d9a3f2c519da283449770571dcc88f54556692c";
        log.trace("Tx hash: {}", txHash);

        debug(txHash, func);
    }
}
