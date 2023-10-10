package my.demo.blockchain_demo.service.core.scenarios.shared;

import com.google.common.base.Preconditions;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.configuration.TreasuryInfo;
import my.demo.blockchain_demo.service.core.contract.FunctionConverter;
import my.demo.blockchain_demo.service.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.service.core.contract.FunctionParser;
import my.demo.blockchain_demo.service.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;

public abstract class TreasuryCall extends SmartContractCall implements Scenario {

    protected TreasuryCall(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder functionEncoder, FunctionParser functionParser, FunctionConverter converter, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, functionEncoder, functionParser, converter, shutdownManager);
    }

    protected String submitTransaction(DeFiFunction func, int treasury) throws Exception {
        Preconditions.checkArgument(0 <= treasury && treasury < appConfiguration.treasuries().size(),
                "Not valid treasury");
        return submitTransaction(func, appConfiguration.treasuries().get(treasury));
    }

    protected String submitTransaction(DeFiFunction func, TreasuryInfo treasury) throws Exception {
        var from = treasury.address();
        var fromPrivateKey = treasury.privateKey();
        return super.submitTransaction(func, from, fromPrivateKey);
    }
}
