package com.ilgrig.tuum.model.account;

import com.ilgrig.tuum.model.balance.CreationBalanceDTO;
import com.ilgrig.tuum.model.balance.ResponseBalanceDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ResponseAccountDTO {

    @NotNull
    @Schema(description = "Account ID", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "Customer ID", example = "1")
    private Long customerId;

    @NotNull
    private List<ResponseBalanceDTO> balances;

}
