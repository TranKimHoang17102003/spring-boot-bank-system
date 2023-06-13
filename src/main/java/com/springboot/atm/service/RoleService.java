package com.springboot.atm.service;

import com.springboot.atm.payload.dto.RoleDto;

import java.util.List;

public interface RoleService {
    public RoleDto save(String name);
    public String delete(String name);
    public List<RoleDto> getAll();
    public RoleDto update(String name);
}
