package com.springboot.atm.service.impl;

import com.springboot.atm.entity.Role;
import com.springboot.atm.exception.AtmAPIException;
import com.springboot.atm.payload.dto.RoleDto;
import com.springboot.atm.repository.RoleRepository;
import com.springboot.atm.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper mapper) {
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }

    @Override
    public RoleDto save(String name) {
        if(roleRepository.existsByName(name)) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Exists role with name " + name);
        }

        Role role = new Role();
        role.setName(name);

        Role role1 = roleRepository.save(role);
        return mapper.map(role1, RoleDto.class);
    }

    @Override
    public String delete(String name) {
        Role role = roleRepository.findByName(name);
        if(role ==null) {
            return "Name not exists";
        }
        roleRepository.delete(role);
        return "Delete success!";
    }

    @Override
    public List<RoleDto> getAll() {
        return roleRepository.findAll().stream().map(role -> mapper.map(role, RoleDto.class)).collect(Collectors.toList());
    }

    @Override
    public RoleDto update(String name) {
        return null;
    }
}
