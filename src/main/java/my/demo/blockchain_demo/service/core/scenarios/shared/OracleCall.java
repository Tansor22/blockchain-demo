package my.demo.blockchain_demo.service.core.scenarios.shared;


import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;

import java.io.IOException;

public abstract class OracleCall<R> extends SmartContractCall implements Scenario<R>{
    protected OracleCall(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    protected String submitTransaction(String data) throws Exception {
        var chainId = appConfiguration.chainId();
        var from = appConfiguration.oracleAddress();
        var fromPrivateKey = appConfiguration.oraclePrivateKey();
        return super.submitTransaction(chainId, data, from, fromPrivateKey);
    }
    protected String callView(String data) throws IOException {
        var oracleAddress = appConfiguration.oracleAddress();
        var smartContractAddress = appConfiguration.oracleAddress();
        return rpcClient.call(oracleAddress, smartContractAddress, data);
    }
}
