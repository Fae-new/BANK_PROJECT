package com.test.bank.entities.Dtos;

import com.test.bank.entities.WalletType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateWalletRequestDto {
    @NotNull(message = "property 'userId' is missing")
    private int userId;
    @NotNull(message = "property 'walletName' is missing")
    private String walletName;
    @NotNull(message = "property 'walletType' is missing")
    private WalletType walletType;

}
