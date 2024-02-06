package my.demo.blockchain_demo.core.scenarios.shared;


import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;

public abstract class OracleCall extends SmartContractCall{

    protected OracleCall(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder encoder, FunctionParser parser, FunctionConverter converter) {
        super(rpcClient, appConfiguration, encoder, parser, converter);
    }

    protected String submitTransaction(DeFiFunction func) throws Exception {
        var from = appConfiguration.oracleAddress();
        var fromPrivateKey = appConfiguration.oraclePrivateKey();
        return super.submitTransaction(func, from, fromPrivateKey);
    }
}
