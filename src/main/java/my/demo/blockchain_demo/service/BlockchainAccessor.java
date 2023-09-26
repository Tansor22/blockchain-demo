package my.demo.blockchain_demo.service;

import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

@Service
public class BlockchainAccessor {
    private static final JsonRpc2_0Web3j RPC_CLIENT = new JsonRpc2_0Web3j(new HttpService(
            "https://fluent-evocative-research.ethereum-sepolia.quiknode.pro/3e103b3b69f2c95034fc761f27801f8b02a6a30a/"
    ));

    // max gas to be burnt by transaction submitting, depends on size of input data
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(100_000);
    public static final BigInteger MAX_PRIORITY_FEE_PER_GAS = BigInteger.valueOf(1_500_000_000);
    private static final Credentials PRIVATE_KEY = Credentials.create("0x94cd2e195a81f1edfcd1a1a8c489340ff51b705af9a5e57f44d2ab2a8ca93f70");

    private static final String ORACLE_ADDRESS = "0x5dD38E3C3ddB60084a0F9c8D8F45B50f68C06f97";
    // WalletLib.sol
    private static final String CONTRACT_ADDRESS = "0x14e5f9b8Acd2B34853BDf38a23Cae06b5C745F34";


    // makeTrade(bytes32 _currency, uint256 _amount, uint256 _orderId, uint256 _code, uint256 deadline)
    private static final Event MAKE_TRADE = new Event("makeTrade", List.of(
            new TypeReference<Bytes32>() {
            },
            new TypeReference<Uint256>() {
            },
            new TypeReference<Uint256>() {
            },
            new TypeReference<Uint256>() {
            },
            new TypeReference<Uint256>() {
            }
    ));
    // makePayout(address  _recipient, bytes32 _currency, uint256 _amount, bytes32 _uniqueId)
    private static final Event MAKE_PAYOUT = new Event("makePayout", List.of(
            new TypeReference<Address>() {
            },
            new TypeReference<Bytes32>() {
            },
            new TypeReference<Uint256>() {
            },
            new TypeReference<Bytes32>() {
            }
    ));


    private BigInteger getNonce(String address) throws IOException {
        var response = RPC_CLIENT.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
        checkResponse(response);
        return response.getTransactionCount();
    }

    private BigInteger getChainId() throws IOException {
        var response = RPC_CLIENT.ethChainId().send();
        checkResponse(response);
        return response.getChainId();
    }

    public String sendTransaction(String transaction) throws IOException {
        var response = RPC_CLIENT.ethSendRawTransaction(transaction).send();
        checkResponse(response);
        return response.getTransactionHash();
    }

    private void checkResponse(Response<?> response) {
        if (response.hasError()) {
            var error = response.getError();
            throw new RuntimeException(error.getMessage());
        }
    }

    private byte[] padBytesWithZeros(byte[] bytes, int size) {
        if (bytes.length >= size) {
            return bytes;
        }
        var output = new byte[size];
        int i;
        for (i = 0; i < bytes.length; i++) {
            output[i] = bytes[i];
        }
        for (int j = i; j < size ; j++) {
            output[j] = 0x0;
        }
        return output;
    }

    private String getMakeTradeData(String currency, long amount, long orderId, long code) {
        var currencyBytes = currency.getBytes();
        var paddedBytes = padBytesWithZeros(currencyBytes, 32);
        var function = new Function(
                MAKE_TRADE.getName(),
                List.of(new Bytes32(paddedBytes),
                        new Uint256(amount),
                        new Uint256(orderId),
                        new Uint256(code),
                        // deadline
                        new Uint256(0)),
                Collections.emptyList());
        return FunctionEncoder.encode(function);
    }

    private String getMakePayoutData(String address, String currency, long amount, long uniqueId) {
        var currencyBytes = currency.getBytes();
        var paddedBytes = padBytesWithZeros(currencyBytes, 32);
        var function = new Function(
                MAKE_PAYOUT.getName(),
                List.of(new Address(address),
                        new Bytes32(paddedBytes),
                        new Uint256(amount),
                        new Uint256(uniqueId)),
                Collections.emptyList());
        return FunctionEncoder.encode(function);
    }
    private BigInteger getMaxFeePerGas() throws IOException {
        var response =
                RPC_CLIENT.ethGetBlockByNumber(DefaultBlockParameterName.PENDING, false).send();
        checkResponse(response);
        var baseFeePerGas = response.getBlock().getBaseFeePerGas();
        return baseFeePerGas.multiply(BigInteger.TWO).add(MAX_PRIORITY_FEE_PER_GAS);
    }

    public void go() throws Exception {
        var chainId = getChainId();
        System.out.println("Chain id:" + chainId);
        var nonce = getNonce(ORACLE_ADDRESS);
        System.out.printf("Nonce for %s: %s\n", ORACLE_ADDRESS, nonce);

        var data = getMakeTradeData("matic", 1, 1, 1);
        // max priority fee per gas, max fee per gas
        var tx = RawTransaction.createTransaction(chainId.longValue(), nonce, GAS_LIMIT, CONTRACT_ADDRESS,
                BigInteger.ZERO, data, MAX_PRIORITY_FEE_PER_GAS, getMaxFeePerGas());

        var signedMessage = TransactionEncoder.signMessage(tx, chainId.longValue(), PRIVATE_KEY);
        var hexValue = Numeric.toHexString(signedMessage);
        var hash = sendTransaction(hexValue);
        // last hash 0x46419c9ce7d62e2c6092baaa7febe98763e357e438b8989115dbfa1ad95460d1
        System.out.printf("Tx %s submitted\n", hash);
    }
}
