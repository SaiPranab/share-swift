package com.shareswift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.shareswift.model.User;
import com.shareswift.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user){
        var newUser = userService.registerLocalUser(user);

        if(newUser != null)
            return "redirect:/files?success=true";
        else 
            return "redirect:/files?success=false";
    }
}