package com.shareswift.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.shareswift.reposiory.UserRepository;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = repository.findByUserEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("UserName Not Found")
            );
        
        return new CustomUser(user);
    }
    
}
