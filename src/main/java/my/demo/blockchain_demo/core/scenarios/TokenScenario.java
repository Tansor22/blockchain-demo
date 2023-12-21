package my.demo.blockchain_demo.core.scenarios;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.configuration.AppConfiguration;
import my.demo.blockchain_demo.core.scenarios.shared.Scenario;
import my.demo.blockchain_demo.core.tokens.DummyToken;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "scenario", havingValue = "token")
public class TokenScenario implements Scenario {
    private final Web3j web3j;
    private final AppConfiguration appConfiguration;
    @Override
    public void go() throws Exception {
        // dummy
        //var tokenAddress = "0x5884339B9f15d92dD998548F3e0CAa749679FD6A";
        // dummy link
        //var tokenAddress = "0x4Acf82A8f6F065784250Ca8499e79Ead5b8f9166";
        // dummy usdt
        //var tokenAddress = "0x23b0A8296136F4ea96c2e1821e267f906F88C411";
        // dummy dai
        var tokenAddress = "0x67DEe3C1D3a0D84E134c6Be2F92e8C472084651f";
        var t = new DummyToken(web3j, tokenAddress, appConfiguration.oracleAddress());
        var name = t.name();
        var symbol = t.symbol();
        var decimals = t.decimals();
        var totalSupply = t.totalSupply();

        log.trace("Token: {}, name: {}, symbol:{}, decimals: {}, totalSupply: {}",
                tokenAddress, name, symbol, decimals, totalSupply);
    }
}
