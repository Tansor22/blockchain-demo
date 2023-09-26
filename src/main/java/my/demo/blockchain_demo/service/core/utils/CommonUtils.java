package my.demo.blockchain_demo.service.core.utils;

import lombok.experimental.UtilityClass;

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

}
