package my.demo.blockchain_demo.service.core.contract.wallet;

import lombok.SneakyThrows;
import my.demo.blockchain_demo.service.core.contract.wallet.entity.Currency;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.List;

public class Wallet extends Contract {
    private static final String BINARY = "Bin file was not provided";

    public Wallet(Web3j web3j, String contractAddress, String fromAddress) {
        super(BINARY, contractAddress, web3j,
                new ReadonlyTransactionManager(web3j, fromAddress), new DefaultGasProvider());
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public List<String> getApprovedCurrencies() {
        final Function func = new Function("getApprovedCurrencyList",
                List.of(),
                List.of(new TypeReference<DynamicArray<Bytes32>>() {
                })
        );
        var result = executeRemoteCallSingleValueReturn(func, DynamicArray.class).send();
        return result.getValue().stream()
                .map(t -> AbiUtils.fromBytes32((Bytes32) t))
                .toList();

    }
    @SneakyThrows
    public Currency getApprovedCurrency(String currency) {
        final Function func = new Function("getApprovedCurrency",
                List.of(AbiUtils.toBytes32((currency))),
                List.of(TypeReference.create(Currency.class))
        );
        return executeRemoteCallSingleValueReturn(func, Currency.class).send();
    }
}



