package com.shareswift.service.impl;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.shareswift.model.User;
import com.shareswift.reposiory.UserRepository;
import com.shareswift.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public User registerLocalUser(User user) {
        var existingUser = userRepository.findByUserEmail(user.getUserEmail());
        
        if(!existingUser.isPresent()){
            var encodedPassword = encoder.encode(user.getUserPassword());
            user.setUserPassword(encodedPassword);
            // user.setUserType("local");
            return userRepository.save(user);
        }

        return null;
    }

    @Override
    public User registerOrgetUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if(authentication instanceof OAuth2AuthenticationToken){
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

            var userEmail = (String) oAuth2User.getAttribute("email");

            var user = userRepository.findByUserEmail(userEmail);
            if(user.isPresent())
                return user.get();
            else {
                // var newUser = registerOAuth2User(authentication);
                // return userRepository.save(newUser);
                var username = (String) oAuth2User.getAttribute("name");
                var providerName = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
                
                var newUser = new User();
                newUser.setUserEmail(userEmail);
                newUser.setUsername(username);
                newUser.setUserType(providerName);

                return userRepository.save(newUser);
            }
        }else {

        }

        var usernamePasswodAuthenticationToken = (UsernamePasswordAuthenticationToken)authentication;
        var userEmail = usernamePasswodAuthenticationToken.getName();
        var user = userRepository.findByUserEmail(userEmail);     
        
        return user.get();
    }
    
    // @Override
    // public User registerOAuth2User(Authentication authentication) {
    //     var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
    //     var providerName = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

    //     var oauth2User = oauth2AuthenticationToken.getPrincipal();
    //     var username = (String) oauth2User.getAttribute("name");
    //     var userEmail = (String) oauth2User.getAttribute("email");

    //     var user = userRepository.findByUserEmail(userEmail);
    //     if (!user.isPresent()) {

    //         var newUser = new User();
    //         newUser.setUserEmail(userEmail);
    //         newUser.setUsername(username);
    //         newUser.setUserType(providerName);

    //         return userRepository.save(newUser);
    //     }
        
    //     return user.get();
    // }

    // @Override
    // public User getLoggedInUser() {
    //     var authentication = SecurityContextHolder.getContext().getAuthentication();
        
    //     if(authentication instanceof OAuth2AuthenticationToken){
    //         var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
    //         OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
    //         var userEmail = (String) oAuth2User.getAttribute("email");

    //         var user = userRepository.findByUserEmail(userEmail);
    //         return user.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email " + userEmail));
    //     }

    //     var usernamePasswodAuthenticationToken = (UsernamePasswordAuthenticationToken)authentication;
    //     var userEmail = usernamePasswodAuthenticationToken.getName();

    //     var user = userRepository.findByUserEmail(userEmail);
    //     return user.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email " + userEmail));
    // }
}
