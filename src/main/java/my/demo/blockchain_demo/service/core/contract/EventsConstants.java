package my.demo.blockchain_demo.service.core.contract;


import lombok.experimental.UtilityClass;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;

@UtilityClass
public class EventsConstants {

    // event that is emitted to charge fees fot contract transactions
    public static Event LOG_FEE_TRANSFER = new Event(
        "LogFeeTransfer",
            List.of(
                    new TypeReference<Address>() {},
                    new TypeReference<Address>() {},
                    new TypeReference<Address>() {},
                    new TypeReference<Uint256>() {},
                    new TypeReference<Uint256>() {},
                    new TypeReference<Uint256>() {},
                    new TypeReference<Uint256>() {},
                    new TypeReference<Uint256>() {}
            )
    );
}
