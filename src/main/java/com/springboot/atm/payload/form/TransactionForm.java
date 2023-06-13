package com.springboot.atm.payload.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Form chuyển tiền")
@Data
public class TransactionForm {
    @NotBlank
    @Schema(description = "Ngân hàng người nhận")
    private String bank;
    @Size(min = 15, max = 15, message = "The account number must be 15 characters long")
    @Schema(description = "Số tài khoản người nhận")
    private String accountNumber;
    @Schema(description = "Lời nhắn")
    private String note;
    @Schema(description = "Số tiền cần chuyển")
    private Double number;
    @NotEmpty
    @Size(min = 6, max = 6, message = "The pin must be 6 characters long")
    @Schema(description = "Nhập xác nhận mã pin của bạn")
    private String pin;
}