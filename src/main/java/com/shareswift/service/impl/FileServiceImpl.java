package com.shareswift.service.impl;

import com.shareswift.model.FileModel;
import com.shareswift.reposiory.FileRepository;
import com.shareswift.service.FileService;
import com.shareswift.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserService userService;

    @Value("${upload.file.dir}")
    private String FILE_DIR;

    @Override
    public void createDIerctory() {
        var file = new File(FILE_DIR);
        if (!file.exists()) {
            file.mkdir();
            log.info("Folder created to store files");
        }

        log.debug("Folder already exists with name " + FILE_DIR);
    }

    // @Override
    // public void uploadFile(MultipartFile file, String uploadedBy) throws IOException {
    //     if (!file.isEmpty()) {
    //         var fileName = file.getOriginalFilename();
    //         var newFileName = generateFileName(fileName);

    //         var fos = new FileOutputStream(FILE_DIR + File.separator + newFileName);
    //         fos.write(file.getBytes());
    //         fos.close();

    //         // var user = userService.getLoggedInUser();
    //         var user = userService.registerOrgetUser();

    //         var fileModel = new FileModel();
    //         fileModel.setFileName(newFileName);
    //         fileModel.setUploadedBy(uploadedBy);
    //         fileModel.setUser(user);
    //         fileModel.setUploadTime(LocalDateTime.now());
    //         fileModel.setExpiryTime(LocalDateTime.now().plusMinutes(1));

    //         fileRepository.save(fileModel);
    //         return;
    //     }
    //     throw new FileNotFoundException("File Not Found");
    // }

    public void uploadFiles(MultipartFile[] files, String uploadedBy) throws IOException {
        // Iterate over all files
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                var fileName = file.getOriginalFilename();
                var newFileName = generateFileName(fileName);
    
                var fos = new FileOutputStream(FILE_DIR + File.separator + newFileName);
                fos.write(file.getBytes());
                fos.close();
    
                // Get or create user (assuming you're registering or fetching the logged-in user)
                var user = userService.registerOrgetUser();
    
                // Create a new file model and save to DB
                var fileModel = new FileModel();
                fileModel.setFileName(newFileName);
                fileModel.setUploadedBy(uploadedBy);
                fileModel.setUser(user);
                fileModel.setUploadTime(LocalDateTime.now());
                fileModel.setExpiryTime(LocalDateTime.now().plusDays(3));
    
                fileRepository.save(fileModel);
            }
        }
    }
    

    @Override
    public List<FileModel> geAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public FileModel shareFile(String fileId) throws IOException{
        if(fileRepository.existsById(fileId)){
            var file = fileRepository.findById(fileId).get();
            return file;
        }

        return null;
    }

    @Override
    public void deleteFile(String fileId) throws FileNotFoundException{
        var fileModel = getFileById(fileId);
        fileRepository.deleteById(fileId);

        var file = new File(FILE_DIR + File.separator + fileModel.getFileName());
        if(file.exists()){
            file.delete();
        }

    }

    @Override
    public ResponseEntity<byte[]> getFileData(String fileId) throws Exception {
        var file = getFileById(fileId);
        var fis = new FileInputStream(FILE_DIR + File.separator + file.getFileName());

        var fileData = fis.readAllBytes();
        fis.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(fileData);
    }

    @Override
    public List<FileModel> getFilesByUserId(String userId) {
        return fileRepository.findAllByUser_UserId(userId);     
    }

    private FileModel getFileById(String fileId) throws FileNotFoundException {
        if (fileRepository.existsById(fileId)) {
            return fileRepository.findById(fileId).get();
        }

        throw new FileNotFoundException("File not found with id " + fileId);
    }

    private String generateFileName(String fileName) {
        var extensionName = fileName.substring(fileName.lastIndexOf("."));
        var newFileName = UUID.randomUUID().toString();
        return newFileName + extensionName;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredFiles() {
        var expiredFiles = fileRepository.findAllByExpiryTimeBefore(LocalDateTime.now());
         
        for (FileModel fileModel : expiredFiles) {
            var file = new File(FILE_DIR + File.separator + fileModel.getFileName());
            if(file.exists()) {
                file.delete();
                fileRepository.delete(fileModel);
                System.out.println("File deleted" + fileModel.getFileName());
            }
        }
    }
}
