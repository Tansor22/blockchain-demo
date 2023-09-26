package my.demo.blockchain_demo.service.core.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class CommonUtils {
    public static byte[] padBytesWithZeros(byte[] bytes, int size) {
        if (bytes.length >= size) {
            return bytes;
        }
        var output = new byte[size];
        int i;
        for (i = 0; i < bytes.length; i++) {
            output[i] = bytes[i];
        }
        for (int j = i; j < size; j++) {
            output[j] = 0x0;
        }
        return output;
    }
    public static byte[] removeTrailingZeros(byte[] b) {
        int notZeroLen = b.length;
        for (int i = b.length - 1; i >= 0; --i, notZeroLen--) {
            if (b[i] != 0) {
                break;
            }
        }

        if (notZeroLen != b.length) {
            b = Arrays.copyOf(b, notZeroLen);
        }

        return b;
    }

}
