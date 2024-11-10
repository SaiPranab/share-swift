package com.shareswift.service;

import com.shareswift.model.User;

public interface UserService {
    User registerLocalUser(User user);

    User registerOrgetUser();

    // User registerOAuth2User(Authentication authentication);

    // User getLoggedInUser();
}
