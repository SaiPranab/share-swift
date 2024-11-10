package com.shareswift.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shareswift.model.User;


public class CustomUser implements UserDetails{
    private User user;

    public CustomUser(User user){
        super();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
       return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserEmail();
    }
    
    
}
