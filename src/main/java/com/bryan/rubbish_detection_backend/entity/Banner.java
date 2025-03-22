package com.bryan.rubbish_detection_backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Banner {
    private Long id;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
