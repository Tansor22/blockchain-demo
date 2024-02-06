package my.demo.blockchain_demo.core.wallet;

import lombok.SneakyThrows;
import my.demo.blockchain_demo.core.wallet.utils.AbiUtils;
import my.demo.blockchain_demo.core.wallet.utils.ContractEx;
import my.demo.blockchain_demo.core.wallet.entity.Currency;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.List;

public class Wallet extends ContractEx {

    public Wallet(Web3j web3j, String contractAddress, String fromAddress) {
        super(web3j, contractAddress, fromAddress);
    }

    public Wallet(Web3j web3j, String contractAddress, TransactionManager txManager) {
        super(web3j, contractAddress, txManager);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public List<String> getApprovedCurrencies() {
        final Function func = new Function("getApprovedCurrencyList",
                List.of(),
                List.of(TypeReference.makeTypeReference("bytes32[]"))
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

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public List<String> getProposedCurrencies() {
        final Function func = new Function("getProposedCurrencyList",
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
    public Currency getProposedCurrency(String currency) {
        final Function func = new Function("getProposedCurrency",
                List.of(AbiUtils.toBytes32((currency))),
                List.of(TypeReference.create(Currency.class))
        );
        return executeRemoteCallSingleValueReturn(func, Currency.class).send();
    }

    @SneakyThrows
    public boolean enableCurrency(String currency) {
        final Function func = new Function("enableCurrency",
                List.of(AbiUtils.toBytes32(currency)),
                List.of()
        );
        var receipt = executeRemoteCallTransaction(func).send();
        return receipt.isStatusOK();
    }

    @SneakyThrows
    public boolean disableCurrency(String currency) {
        final Function func = new Function("disableCurrency",
                List.of(AbiUtils.toBytes32(currency)),
                List.of()
        );
        var receipt = executeRemoteCallTransaction(func).send();
        return receipt.isStatusOK();
    }

   @SneakyThrows
    public boolean removeApprovedCurrency(String currency) {
        final Function func = new Function("removeApprovedCurrency",
                List.of(AbiUtils.toBytes32(currency)),
                List.of()
        );
        var receipt = executeRemoteCallTransaction(func).send();
        return receipt.isStatusOK();
    }

    @SneakyThrows
    public boolean proposeCurrency(String currency, String address, BigInteger min, BigInteger max) {
        final Function func = new Function("proposeCurrency",
                List.of(AbiUtils.toBytes32(currency), new Address(address), new Uint(min), new Uint(max)),
                List.of()
        );
        var receipt = executeRemoteCallTransaction(func).send();
        return receipt.isStatusOK();
    }

    @SneakyThrows
    public boolean approveProposedCurrency(String currency, String address, BigInteger min, BigInteger max) {
        final Function func = new Function("approveProposedCurrency",
                List.of(AbiUtils.toBytes32(currency), new Address(address), new Uint(min), new Uint(max)),
                List.of()
        );
        var receipt = executeRemoteCallTransaction(func).send();
        return receipt.isStatusOK();
    }

    @SneakyThrows
    public String getOracleAddress() {
        final Function func = new Function("getOracleAddress",
                List.of(),
                List.of(TypeReference.create(Address.class))
        );
        var address = executeRemoteCallSingleValueReturn(func, Address.class).send();
        return address.getValue();
    }

    @SneakyThrows
    public BigInteger getBalance(String currency) {
        final Function func = new Function("getBalance",
                List.of(AbiUtils.toBytes32(currency.toLowerCase())),
                List.of(TypeReference.create(Uint.class))
        );
        var balance = executeRemoteCallSingleValueReturn(func, Uint.class).send();
        return balance.getValue();
    }

}



