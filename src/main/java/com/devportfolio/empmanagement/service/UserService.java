package com.devportfolio.empmanagement.service;

import com.devportfolio.empmanagement.dto.UserRegistrationDto;
import com.devportfolio.empmanagement.model.User;

public interface UserService {
    boolean isUsernameTaken(String username);
    User registerNewUser(UserRegistrationDto dto);
}