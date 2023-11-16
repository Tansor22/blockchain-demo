package my.demo.blockchain_demo.core.contract;

import lombok.experimental.UtilityClass;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.util.Arrays;

@UtilityClass
public class AbiUtils {

    public static Bytes32 toBytes32(String s) {
        var bytes = padBytes32WithZeros(s.getBytes());
        return new Bytes32(bytes);
    }

    public static String fromBytes32(Bytes32 b) {
        var bytes = b.getValue();
        return new String(removeTrailingZeros(bytes));
    }

    private static byte[] padBytes32WithZeros(byte[] bytes) {
        if (bytes.length >= 32) {
            return bytes;
        }
        var output = new byte[32];
        System.arraycopy(bytes, 0, output, 0, bytes.length);
        for (int j = bytes.length; j < 32; j++) {
            output[j] = 0x0;
        }
        return output;
    }

    private static byte[] removeTrailingZeros(byte[] b) {
        int notZeroLen = b.length;
        for (int i = b.length - 1; i >= 0; --i) {
            if (b[i] == 0) {
                notZeroLen--;
            } else {
                break;
            }
        }
        return Arrays.copyOf(b, notZeroLen);
    }
}

