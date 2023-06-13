package com.springboot.atm.controller;

import com.springboot.atm.entity.User;
import com.springboot.atm.payload.dto.JWTAuthResponse;
import com.springboot.atm.payload.form.LoginForm;
import com.springboot.atm.payload.form.RegisterForm;
import com.springboot.atm.repository.UserRepository;
import com.springboot.atm.service.AuthService;
import com.springboot.atm.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Authentication", description = "APIs for managing login / logout")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository=userRepository;
    }


    @PostMapping(value = {"/api/v1/auth/login"})
    @Operation(summary = "Đăng nhập")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody @Valid LoginForm loginForm){
        String token = authService.login(loginForm);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping(value = {"/api/v1/auth/register"})
    @Operation(summary = "Đăng kí")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterForm registerForm){
        String response = authService.register(registerForm);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/auth/my-info")
    @Operation(summary = "Xem thông tin của tôi")
    public ResponseEntity getMyInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with account number " + name));
        return new ResponseEntity(authService.getInfo(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/api/v1/auth/admin/get-all")
    @Operation(summary = "Xem tất cả tài khoản <ADMIN>")
    public ResponseEntity getAll(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER , required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE , required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY , required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION , required = false) String sortDir
    ) {
        return new ResponseEntity<>(authService.getAll(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/api/v1/auth/admin/user/{id}")
    @Operation(summary = "Xem thôn tin người dùng bằng id <ADMIN>")
    public ResponseEntity getUserInfo(@PathVariable("id") Long id) {
        return new ResponseEntity(authService.getInfo(id), HttpStatus.OK);
    }
}