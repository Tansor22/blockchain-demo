package my.demo.blockchain_demo.service.core.scenarios.shared;

import com.google.common.base.Preconditions;
import my.demo.blockchain_demo.service.configuration.AppConfiguration;
import my.demo.blockchain_demo.service.core.contract.OnChainDataParser;
import my.demo.blockchain_demo.service.core.contract.OnChainEncoder;
import my.demo.blockchain_demo.service.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.service.shutdown.ApplicationShutdownManager;

public abstract class TreasuryCall<R> extends SmartContractCall implements Scenario<R> {
    protected TreasuryCall(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, OnChainEncoder onChainEncoder, OnChainDataParser onChainDataParser, ApplicationShutdownManager shutdownManager) {
        super(rpcClient, appConfiguration, onChainEncoder, onChainDataParser, shutdownManager);
    }

    protected String submitTransaction(int treasury, String data) throws Exception {
        Preconditions.checkArgument(0 <= treasury && treasury < appConfiguration.treasuries().size(),
                "Not valid treasury");
        var treasuryInfo = appConfiguration.treasuries().get(treasury);
        var chainId = appConfiguration.chainId();
        var from = treasuryInfo.address();
        var fromPrivateKey = treasuryInfo.privateKey();
        return super.submitTransaction(chainId, data, from, fromPrivateKey);
    }
}
