package com.springboot.atm.controller;

import com.springboot.atm.service.NotifyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotifyController {
    private final NotifyService notifyService;

    public NotifyController(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @GetMapping("api/v1/notify/my-notify")
    @Operation(summary = "Xem thông báo của tôi")
    public ResponseEntity getMyNotify() {
        return new ResponseEntity<>(notifyService.getMyNotify(), HttpStatus.OK);
    }

    @GetMapping("api/v1/notify/admin/get-all")
    @Operation(summary = "Xem tất cả thông báo của USER <ADMIN>")
    public ResponseEntity getAll() {
        return new ResponseEntity<>(notifyService.getAll(), HttpStatus.OK);
    }

    @GetMapping("api/v1/notify/admin/get-user/{id}")
    @Operation(summary = "Xem thông báo của USER bằng id <ADMIN>")
    public ResponseEntity getByUserId(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(notifyService.getByUserId(userId), HttpStatus.OK);
    }
}
