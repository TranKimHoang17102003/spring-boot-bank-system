package com.springboot.atm.service;

import com.springboot.atm.entity.Notify;
import com.springboot.atm.payload.dto.NotifyDto;

import java.util.List;

public interface NotifyService {
    List<NotifyDto> getMyNotify();
    List<NotifyDto> getAll();
    List<NotifyDto> getByUserId(Long userId);
}
