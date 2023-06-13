package com.springboot.atm.payload.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Schema(description = "Form đăng kí")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
    @NotBlank(message = "Username is required")
    @Schema(description = "Username")
    private String username;
    @NotBlank(message = "Password is required")
    @Schema(description = "Mật khẩu")
    private String password;
    @NotBlank
    @Schema(description = "Nhập lại mật khẩu")
    private String checkPass;
    @NotBlank
    @Size(min = 6, max = 6, message = "The pin must be 6 characters long")
    @Schema(description = "Mã pin")
    private String pin;
    @NotBlank
    @Size(min = 6, max = 6, message = "The pin must be 6 characters long")
    @Schema(description = "Nhập lại mã pin")
    private String checkPin;
    @Email(message = "Invalid email format")
    @Schema(description = "Email của bạn")
    private String email;
    @Size(min = 10, max = 10, message = "The phone number must be 10 characters long")
    @Schema(description = "Số điện thoại có 10 số")
    private String phone;
    @NotNull
    @Schema(description = "First name")
    private String firstName;
    @NotNull
    @Schema(description = "Last name")
    private String lastName;
    @NotNull
    @Schema(description = "Ngày sinh")
    private LocalDate birthday;
}
