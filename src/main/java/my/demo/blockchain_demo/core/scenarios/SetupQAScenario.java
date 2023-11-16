package my.demo.blockchain_demo.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.configuration.TreasuryInfo;
import my.demo.blockchain_demo.core.contract.FunctionConverter;
import my.demo.blockchain_demo.core.contract.FunctionEncoder;
import my.demo.blockchain_demo.core.contract.FunctionParser;
import my.demo.blockchain_demo.core.contract.functions.DeFiFunction;
import my.demo.blockchain_demo.core.contract.functions.GetApprovedCurrency;
import my.demo.blockchain_demo.core.contract.functions.GetProposedCurrency;
import my.demo.blockchain_demo.core.rpc.EthJsonRpcExt;
import my.demo.blockchain_demo.core.scenarios.shared.TreasuryCall;
import my.demo.blockchain_demo.core.utils.CommonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "setupQA")
public class SetupQAScenario extends TreasuryCall {

    protected SetupQAScenario(EthJsonRpcExt rpcClient, AppConfiguration appConfiguration, FunctionEncoder encoder, FunctionParser parser, FunctionConverter converter) {
        super(rpcClient, appConfiguration, encoder, parser, converter);
    }

    boolean zeroTreasuryBalance(TreasuryInfo treasury) throws Exception {
        var balance = rpcClient.getBalance(treasury.address());
        return balance.compareTo(BigInteger.ZERO) <= 0;
    }

    String call(TreasuryInfo treasury, DeFiFunction func) throws Exception {
        return submitTransaction(func, treasury);
    }

    List<String> getProposedCurrency(String currency) throws Exception {
        var proposedCurrencyFunc = new GetProposedCurrency(currency);
        var proposedCurrencyReturn = callView(proposedCurrencyFunc);
        return parser.parseFunctionReturnAsStrings(proposedCurrencyReturn, proposedCurrencyFunc);
    }

    List<String> getApprovedCurrency(String currency) throws Exception {
        var approvedCurrencyFunc = new GetApprovedCurrency(currency);
        var proposedCurrencyReturn = callView(approvedCurrencyFunc);
        return parser.parseFunctionReturnAsStrings(proposedCurrencyReturn, approvedCurrencyFunc);
    }

    @Override
    public void go() throws Exception {
        var firstTreasury = appConfiguration.treasuries().get(0);
        var secondTreasury = appConfiguration.treasuries().get(1);
        if (zeroTreasuryBalance(firstTreasury)) {
            log.error("Zero balance of first treasury at {}:", firstTreasury.address());
            return;
        }

        if (zeroTreasuryBalance(secondTreasury)) {
            log.error("Zero balance of second treasury at {}:", secondTreasury.address());
            return;
        }
        for (var coin : appConfiguration.coins()) {
            var name = CommonUtils.padTo(Numeric.toHexString(coin.name().getBytes()), '0', 66);
            var min = coin.minEncoded();
            var max = coin.maxEncoded();


            log.trace("{}: {}, min: {}, max: {}, address: {}", coin.name(), name, min, max, coin.address());
        }
        /*for (var coin : appConfiguration.coins()) {
            //var coinEncoded = CommonUtils.toHex32(coin.name());
            var minEncoded = CommonUtils.encodeAmount(coin.min(), coin.decimals());
            var maxEncoded = CommonUtils.encodeAmount(coin.max(), coin.decimals());

            var proposeFunc = new ProposeCurrencyFunction(coin.name(), coin.address(), minEncoded, maxEncoded);
            call(firstTreasury, proposeFunc);
            var proposedCurrency = getProposedCurrency(coin.name());
            log.trace("{} proposed currency {}, return {} ", firstTreasury.address(), coin.name(), proposedCurrency);

            var approveCurrency = new ApproveProposedCurrency(coin.name(), coin.address(), minEncoded, maxEncoded);
            call(secondTreasury, approveCurrency);

            var approvedCurrency = getApprovedCurrency(coin.name());
            log.trace("{} approved currency {}, return {} ", firstTreasury.address(), coin.name(), approvedCurrency);

        }*/

    }
}
