package com.springboot.atm.payload.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Form đăng nhập")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
    @Schema(description = "Username người dùng")
    @NotBlank
    private String username;
    @Schema(description = "Mật khẩu")
    @NotBlank
    private String password;
}
