package my.demo.blockchain_demo.service.core.rpc;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;

public class EthJsonRpcExt extends JsonRpc2_0Web3j {
    public EthJsonRpcExt(Web3jService web3jService) {
        super(web3jService);
    }

    public BigInteger getNonce(String address) throws IOException {
        var response = this.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
        checkResponse(response);
        return response.getTransactionCount();
    }

    public EthBlock.Block getPendingBlock() throws IOException, RpcException {
        var response = rpc.ethGetBlockByNumber(DefaultBlockParameterName.PENDING, false).send();
        checkResponse(response);
        return response.getBlock();
    }

    public String call(String from, String to, String data) throws IOException {
        var request = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(from, to, data);
        var response = this.ethCall(request, DefaultBlockParameterName.LATEST).send();
        checkResponse(response);
        return response.getValue();
    }

    public BigInteger getBalance(String address) throws IOException {
        var response = this.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        checkResponse(response);
        return response.getBalance();
    }

    public long getChainId() throws IOException {
        var response = this.ethChainId().send();
        checkResponse(response);
        return response.getChainId().longValue();
    }

    public Transaction getTransaction(String hash) throws IOException {
        var response = this.ethGetTransactionByHash(hash).send();
        checkResponse(response);
        return response.getTransaction().orElse(null);
    }

    public TransactionReceipt getTransactionReceipt(String hash) throws IOException {
        var response = this.ethGetTransactionReceipt(hash).send();
        checkResponse(response);
        return response.getTransactionReceipt().orElse(null);
    }

    public String sendTransaction(String transaction) throws IOException {
        var response = this.ethSendRawTransaction(transaction).send();
        checkResponse(response);
        return response.getTransactionHash();
    }

    public EthBlock.Block getBlockByNumber() throws IOException {
        var response =
                this.ethGetBlockByNumber(DefaultBlockParameterName.PENDING, false).send();
        checkResponse(response);
        return response.getBlock();
    }

    private void checkResponse(Response<?> response) {
        if (response.hasError()) {
            var error = response.getError();
            throw new RuntimeException(error.getMessage());
        }
    }
}
