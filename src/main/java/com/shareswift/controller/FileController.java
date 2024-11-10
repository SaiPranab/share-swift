package com.shareswift.controller;

import com.shareswift.model.User;
import com.shareswift.service.FileService;
import com.shareswift.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @GetMapping()
    public String landingPage() {
        return "landing-page";
    }

    // @PostMapping("/local-home")
    // public String localHome(Model model) {
    // model.addAttribute("name", fileService.getUsername());
    // model.addAttribute("files", fileService.getUserFiles());
    // return "home";
    // }

    // @GetMapping("/oauth2-home")
    // public String home(Authentication authentication, Model model) {
    // userService.registerOAuth2User(authentication);
    // model.addAttribute("files", fileService.geAllFiles());
    // return "home";
    // }

    @GetMapping("/home")
    public String home(Model model) {
        User user = userService.registerOrgetUser();

        model.addAttribute("name", user.getUsername());
        model.addAttribute("files", fileService.getFilesByUserId(user.getUserId()));

        return "home";
    }

    @PostMapping("/upload")
    public String uploadFiles(@RequestParam("files") MultipartFile[] files,
            @RequestParam("uploadedBy") String uploadedBy) throws IOException {
        fileService.uploadFiles(files, uploadedBy);
        return "redirect:/files/home";
    }

    @GetMapping("/share/{id}")
    public String shareFile(@PathVariable("id") String fileId, Model model)
            throws IOException {
        var file = fileService.shareFile(fileId);

        if (file != null) {
            var url = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();

            model.addAttribute("url", url);
            model.addAttribute("file", file);

            return "share-file";
        } else {
            return "redirect:/files/home";
        }
    }

    @PostMapping("/delete/{fileId}")
    public String File(@PathVariable String fileId, HttpServletResponse response)
            throws FileNotFoundException {
        fileService.deleteFile(fileId);
        return "redirect:/files/home";
    }

    @ResponseBody
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) throws Exception {
        return fileService.getFileData(fileId);
    }
}
