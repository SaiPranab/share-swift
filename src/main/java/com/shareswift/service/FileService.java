package com.shareswift.service;

import com.shareswift.model.FileModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileService {
    void createDIerctory();
    
    void uploadFiles(MultipartFile[] files, String uploadedBy) throws IOException;

    List<FileModel> geAllFiles();

    FileModel shareFile(String fileId) throws IOException;

    void deleteFile(String fileId) throws FileNotFoundException;

    ResponseEntity<byte[]> getFileData(String fileId) throws Exception;

    List<FileModel> getFilesByUserId(String userId);

    void deleteExpiredFiles();

}
