package com.devportfolio.empmanagement.serviceImp;

import java.util.Collections;
import java.util.HashSet;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devportfolio.empmanagement.dto.UserRegistrationDto;
import com.devportfolio.empmanagement.model.Role;
import com.devportfolio.empmanagement.model.User;
import com.devportfolio.empmanagement.repository.RoleRepository;
import com.devportfolio.empmanagement.repository.UserRepository;
import com.devportfolio.empmanagement.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public User registerNewUser(UserRegistrationDto dto) {
        // 1. ROLE_USER ढूँढें या बनाएँ
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        // 2. नया User बनाएँ
        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // BCrypt Hashing
        user.setEnabled(true);
        user.setRoles(new HashSet<>(Collections.singletonList(userRole))); // हमेशा ROLE_USER मिलेगा

        // 3. MySQL में सेव करें
        return userRepository.save(user);
    }
}