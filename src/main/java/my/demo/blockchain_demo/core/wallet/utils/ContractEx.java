package my.demo.blockchain_demo.core.wallet.utils;

import lombok.SneakyThrows;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.List;

public class ContractEx extends Contract {

    // readonly
    protected ContractEx(Web3j web3j, String contractAddress, String fromAddress) {
        this(web3j, contractAddress, new ReadonlyTransactionManager(web3j, fromAddress));
    }

    protected ContractEx(Web3j web3j, String contractAddress, TransactionManager txManager) {
        super(Contract.BIN_NOT_PROVIDED, contractAddress, web3j, txManager, new DefaultGasProvider());
    }

    @SneakyThrows
    protected <T extends Type<?>> T noParamsSingleValueReturn(String funcName, Class<T> returnType) {
        final Function func = new Function(funcName,
                List.of(),
                List.of(TypeReference.create(returnType))
        );
        return executeRemoteCallSingleValueReturn(func, returnType).send();
    }

}
