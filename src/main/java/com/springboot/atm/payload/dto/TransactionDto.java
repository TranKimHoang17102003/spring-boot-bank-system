package com.springboot.atm.payload.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Biên lai giao dịch")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    @Schema(description = "Mã id")
    private Long id;
    @Schema(description = "Số tài khoản người chuyển")
    private String accountNumberRemitter;
    @Schema(description = "Số tài khoản người nhận")
    private String accountNumberReceiver ;
    @Schema(description = "Lời nhắn")
    private String note;
    @Schema(description = "Tên người chuyển")
    private String nameRemitter;
    @Schema(description = "Tên người nhận")
    private String nameReceiver;
    @Schema(description = "Ngân hàng người nhận")
    private String bank;
    @Schema(description = "Thời gian chuyển")
    private LocalDateTime createAt;
    @Schema(description = "Số tiền chuyển")
    private Double number;
}