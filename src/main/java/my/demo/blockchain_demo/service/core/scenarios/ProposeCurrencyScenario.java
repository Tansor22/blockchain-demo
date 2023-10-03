package my.demo.blockchain_demo.service.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.core.scenarios.shared.TreasuryCall;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Function;

import java.util.Map;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "proposeCurrency")
public class ProposeCurrencyScenario extends TreasuryCall<Map<String, ?>> {
    protected ProposeCurrencyScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    @Override
    public Map<String, ?> go() throws Exception {
        var currency = "matic";
        var contractAddress = appConfiguration.smartContractAddress();
        var min = 1;
        var max = 1_000;
        var data = onChainEncoder.encodeProposeCurrency(currency, contractAddress, min, max);
        var txHash = submitTransaction(0, data);
        log.trace("Tx hash: {}", txHash);

        var func = onChainEncoder.getProposedCurrency(currency);
        var funcData = onChainEncoder.encodeFunctionCall(func);
        var parsed = onChainDataParser.parseFunctionReturnAsMap(funcData, func);
        log.trace("Currency {} : {}", currency, parsed);
        return parsed;
    }
}
