package com.shareswift.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String fileId;

    private String fileName;

    private String uploadedBy;

    private LocalDateTime uploadTime;

    private LocalDateTime expiryTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
