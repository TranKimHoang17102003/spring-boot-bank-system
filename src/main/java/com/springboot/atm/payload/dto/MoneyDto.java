package com.springboot.atm.payload.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Biên lai nạp / rút tiền")
@Getter
@Setter
@NoArgsConstructor
public class MoneyDto {
    @Schema(description = "Số tiền vùa rút / nạp")
    private Double number;
    @Schema(description = "Tên của tôi")
    private String name;
    @Schema(description = "Số tài khoản của tôi")
    private String accountNumber;
    @Schema(description = "Ngân hàng của tôi")
    private String bank;

    public MoneyDto(Double number, String name, String accountNumber, String bank) {
        this.number = number;
        this.name = name;
        this.accountNumber = accountNumber;
        this.bank = bank;
    }
}
