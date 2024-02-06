package my.demo.blockchain_demo.core.scenarios;

import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.core.scenarios.shared.Scenario;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
@ConditionalOnProperty(value = "scenario", havingValue = "run")
public class Runner implements Scenario {
    private String retrieveMethodName(String s){
        return s.split(":")[0];
    }

    private String retrieveAdapterFromFxpayPluginParams(String parameters) {
        var adapterPattern = Pattern.compile("adapter=(.+?)(&|\\z)");
        var adapterMatcher = adapterPattern.matcher(parameters);
        return adapterMatcher.find() ? adapterMatcher.group(1) : "FXPAY";
    }
    @Override
    public void go() throws Exception {
        var src = "pay_tron_node_push_usdt:features=&adapter=TRON-CEX&paymentSolution=TRON&workflow=CryptoPush&method=CRYPTO_PUSH&currency_list=USDT&account_currency_list=USDT&account_codes=TRON_USDT_CRYP&paymentGateway=DIRECT,\n" +
                "pay_tron_node_out_usdt:features=&adapter=TRON-CEX&paymentSolution=TRON&workflow=CryptoWithdrawal&method=CRYPTO&currency_list=USDT&account_currency_list=USDT&account_codes=TRON_USDT_STORM&paymentGateway=DIRECT,\n" +
                "pay_tron_push_usdt:workflow=CryptoPush&optional_fields=subOrgRequisiteBucket&currency_list=USDT&account_currency_list=USDT&account_codes=TRON_GLOX&paymentSolution=TRON&method=CRYPTO_PUSH,\n" +
                "pay_explorer_push_usdt_via_eth:workflow=CryptoPush&optional_fields=subOrgRequisiteBucket&currency_list=USDT&account_currency_list=USDT&account_codes=DIRECT_INSTANCE_XCOEX_USDT&paymentSolution=EXPLORER&method=CRYPTO_PUSH,\n" +
                "pay_eth_node_push_usdt:features=&adapter=ETHEREUM-CEX&paymentSolution=ETHEREUM&workflow=CryptoPush&method=CRYPTO_PUSH&currency_list=USDT&account_currency_list=USDT&account_codes=ETHEREUM_USDT_CRYP&paymentGateway=DIRECT";
        for (String s : src.split(",")) {
            var method = retrieveMethodName(s);
            var adapter = retrieveAdapterFromFxpayPluginParams(s);
            log.trace("{} -> {}", method, adapter);
        }

    }
}
