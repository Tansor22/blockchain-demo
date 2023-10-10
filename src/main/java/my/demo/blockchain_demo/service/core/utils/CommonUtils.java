package my.demo.blockchain_demo.service.core.utils;

import lombok.experimental.UtilityClass;
import org.bouncycastle.util.encoders.Hex;

import java.util.Arrays;

@UtilityClass
public class CommonUtils {
    private final byte[] HEX_PREFIX_BYTES_ENCODED = new byte[]{48, 120};

    public static byte[] paddedBytes32(String s) {
        return padBytesWithZeros(s.getBytes());
    }

    public static String paddedBytes32(byte[] b) {
        var bytes =  removeTrailingZeros(b);
        return new String(bytes);
    }

    public static byte[] toHex32Bytes(String s, boolean prefix) {
        var hexBytes = Hex.encode(s.getBytes());
        var appended = prefix ? appendBytes(hexBytes, HEX_PREFIX_BYTES_ENCODED) : hexBytes;
        return padBytesEncodedWithZeros(appended);
    }

    public static String toHex32String(String s, boolean prefix) {
        var hex32Bytes = toHex32Bytes(s, prefix);
        return new String(hex32Bytes);
    }

    public static String fromHex32Bytes(byte[] b) {
        // reverse order
        var nonPadded = removeTrailing(b, (byte) 48);
        var clean = cleanPrefixBytesEncoded(nonPadded);

        var decoded = Hex.decode(clean);


        return new String(decoded);
    }

    public static String fromHex32String(String s) {
        // reverse order
        var bytes = removeTrailing(s.getBytes(), (byte) 48);
        var clean = cleanPrefixBytesEncoded(bytes);
        var decoded = Hex.decode(clean);
        return new String(decoded);
    }

    private byte[] cleanPrefixBytesEncoded(byte[] b) {
        if (b.length >= 2 && b[0] == HEX_PREFIX_BYTES_ENCODED[0] && b[1] == HEX_PREFIX_BYTES_ENCODED[1]) {
            var output = new byte[b.length - 2];

            for (int i = 0; i < output.length; i++) {
                output[i] = b[i + 2];
            }
            return output;
        }
        return b;
    }

    public byte[] appendBytes(byte[] right, byte[] left) {
        var output = new byte[right.length + left.length];
        var index = 0;
        for (int i = 0; i < left.length; i++, index++) {
            output[index] = left[i];
        }
        for (int i = 0; i < right.length; i++, index++) {
            output[index] = right[i];
        }
        return output;
    }

    public static String padTo(String s, char symbol, int length) {
        if (s.length() >= length) {
            return s;
        }
        var diff = length - s.length();
        var suffix = String.valueOf(symbol).repeat(diff);
        return s + suffix;
    }

    public static String cutToLast(String target, char symbol) {
        int index = -1;
        for (int i = target.length() - 1; i >= 0; i--) {
            if (target.charAt(i) == symbol) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            return target.substring(index + 1);
        } else {
            return target;
        }
    }

    public static byte[] padBytesEncodedWithZeros(byte[] bytes) {
        return padBytes32With(bytes, (byte) 48);
    }

    public static byte[] padBytesWithZeros(byte[] bytes) {
        return padBytes32With(bytes, (byte) 0);
    }

    public static byte[] padBytes32With(byte[] bytes, byte b) {
        if (bytes.length >= 32) {
            return bytes;
        }
        var output = new byte[32];
        int i;
        for (i = 0; i < bytes.length; i++) {
            output[i] = bytes[i];
        }
        for (int j = i; j < 32; j++) {
            output[j] = b;
        }
        return output;
    }

    public static byte[] removeTrailingZeros(byte[] b) {
        return removeTrailing(b, (byte) 0);
    }

    private static byte[] removeTrailing(byte[] bb, byte b) {
        int notZeroLen = bb.length;
        for (int i = bb.length - 1; i >= 0; --i, notZeroLen--) {
            if (bb[i] != b) {
                break;
            }
        }

        if (notZeroLen != bb.length) {
            bb = Arrays.copyOf(bb, notZeroLen);
        }

        return bb;
    }

}
