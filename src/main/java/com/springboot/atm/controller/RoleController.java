package com.springboot.atm.controller;

import com.springboot.atm.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/api/v1/role/admin")
    @Operation(summary = "Xem tất cả ROLE của he thong <ADMIN>")
    private ResponseEntity getAll() {
        return new ResponseEntity<>(roleService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/api/v1/role/admin")
    @Operation(summary = "Tao ROLE <ADMIN>")
    private ResponseEntity save(@RequestParam("name") String name) {
        return new ResponseEntity<>(roleService.save(name), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/role/admin")
    @Operation(summary = "Xoa ROLE <ADMIN>")
    private ResponseEntity delete(@RequestParam("name") String name) {
        return new ResponseEntity<>(roleService.delete(name), HttpStatus.OK);
    }
}
