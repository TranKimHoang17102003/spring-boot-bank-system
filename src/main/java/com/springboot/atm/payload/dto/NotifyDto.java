package com.springboot.atm.payload.dto;

import lombok.Data;

@Data
public class NotifyDto {
    private Long id;
    private String title;
    private String content;
    private String note;
    private Long userId;
}
