package my.demo.blockchain_demo.core.wallet.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.reflection.Parameterized;

import java.math.BigInteger;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class Currency extends DynamicStruct {
    private final String address;
    private final BigInteger minAmount;
    private final BigInteger maxAmount;
    private final boolean active;
    private final boolean enabled;
    private final List<String> approvedBy;


    public Currency(String address, BigInteger minAmount, BigInteger maxAmount,
                    boolean active, boolean enabled, List<String> approvedBy) {
        super(new Address(address), new Uint(minAmount), new Uint(maxAmount),
                new Bool(active), new Bool(enabled), new DynamicArray<>(Address.class, approvedBy.stream().map(Address::new).toList()));
        this.address = address;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.active = active;
        this.enabled = enabled;
        this.approvedBy = approvedBy;
    }

    public Currency(Address address, Uint minAmount, Uint maxAmount,
                    Bool active, Bool enabled, @Parameterized(type = Address.class) DynamicArray<Address> approvedBy) {
        super(address, maxAmount, maxAmount, active, enabled, approvedBy);
        this.address = address.getValue();
        this.minAmount = minAmount.getValue();
        this.maxAmount = maxAmount.getValue();
        this.active = active.getValue();
        this.enabled = enabled.getValue();
        this.approvedBy = approvedBy.getValue().stream().map(Address::getValue).toList();
    }
}
