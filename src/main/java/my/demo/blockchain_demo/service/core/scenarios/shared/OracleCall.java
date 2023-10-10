package my.demo.blockchain_demo.service.core.scenarios.shared;


import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.FunctionConverter;
import my.demo.blockchain_demo.service.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.service.core.contract.FunctionParser;
import my.demo.blockchain_demo.service.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;

public abstract class OracleCall extends SmartContractCall implements Scenario{

    protected OracleCall(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder functionEncoder, FunctionParser functionParser, FunctionConverter converter, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, functionEncoder, functionParser, converter, shutdownManager);
    }

    protected String submitTransaction(DeFiFunction func) throws Exception {
        var from = appConfiguration.oracleAddress();
        var fromPrivateKey = appConfiguration.oraclePrivateKey();
        return super.submitTransaction(func, from, fromPrivateKey);
    }
}
