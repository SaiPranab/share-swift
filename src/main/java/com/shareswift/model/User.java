package com.shareswift.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String username;
    
    private String userEmail;
    
    private String userPassword;

    private String userType;

    @OneToMany(
        mappedBy = "user"
    )
    private List<FileModel> userFiles;
}
