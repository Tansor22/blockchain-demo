package my.demo.blockchain_demo.service.core;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import my.demo.blockchain_demo.service.core.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class StringsParser {
    public @Nullable String parseString(Type abi) {
        if (abi instanceof Bytes32 bytes32) {
            var rawBytes = bytes32.getValue();
            var bytes = CommonUtils.removeTrailingZeros(rawBytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } else {
            log.error("Incorrect type for input {}, should be bytes32.", abi.getTypeAsString());
        }
        return null;
    }
}
