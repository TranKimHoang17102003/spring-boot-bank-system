package com.springboot.atm.payload.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Form nạp / rút tiền")
@Data
public class MoneyForm {
    @NotEmpty
    @Size(min = 15, max = 15, message = "The account number must be 15 characters long")
    @Schema(description = "Số tài khoản của bạn")
    private String accountNumber;
    @NotEmpty
    @Size(min = 6, max = 6, message = "The pin must be 6 characters long")
    @Schema(description = "Mã pin của bạn")
    private String pin;
    @NotNull
    @Schema(description = "Số tiền cần rút / nạp")
    private Double number;
}
