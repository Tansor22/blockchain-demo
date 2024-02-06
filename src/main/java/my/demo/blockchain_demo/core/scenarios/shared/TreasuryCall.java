package my.demo.blockchain_demo.core.scenarios.shared;

import com.google.common.base.Preconditions;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.configuration.TreasuryInfo;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;

public abstract class TreasuryCall extends SmartContractCall {

    protected TreasuryCall(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder encoder, FunctionParser parser, FunctionConverter converter) {
        super(rpcClient, appConfiguration, encoder, parser, converter);
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
