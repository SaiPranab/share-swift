package com.shareswift.reposiory;

import com.shareswift.model.FileModel;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileModel, String> {
    List<FileModel> findAllByUser_UserId(String userId);

    List<FileModel> findAllByExpiryTimeBefore(LocalDateTime time);
}
