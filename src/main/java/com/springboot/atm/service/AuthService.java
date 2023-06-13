package com.springboot.atm.service;

import com.springboot.atm.entity.User;
import com.springboot.atm.payload.dto.UserDto;
import com.springboot.atm.payload.dto.UserListResponse;
import com.springboot.atm.payload.form.LoginForm;
import com.springboot.atm.payload.form.RegisterForm;

import java.util.List;

public interface AuthService {
    String login(LoginForm loginDto);

    String register(RegisterForm registerDto);

    UserDto getInfo(Long id);
    UserListResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir);
}
