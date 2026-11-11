package com.devportfolio.empmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.devportfolio.empmanagement.dto.UserRegistrationDto;
import com.devportfolio.empmanagement.service.UserService;

import jakarta.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                                      BindingResult result) {

        // 1. चेक करें कि क्या Username पहले से किसी और का है
        if (userService.isUsernameTaken(userDto.getUsername())) {
            result.addError(new FieldError("userDto", "username", "Username is already taken!"));
        }

        // 2. चेक करें कि Password और Confirm Password मैच हो रहे हैं या नहीं
        if (userDto.getPassword() != null && userDto.getConfirmPassword() != null) {
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                result.addError(new FieldError("userDto", "confirmPassword", "Passwords do not match!"));
            }
        }

        // अगर कोई वैलिडेशन एरर है तो फॉर्म दोबारा दिखाएँ
        if (result.hasErrors()) {
            return "register";
        }

        // सब कुछ सही है, डेटाबेस में यूज़र रजिस्टर करें
        userService.registerNewUser(userDto);

        // लॉगिन पेज पर सफलता का फ्लैग लेकर रीडायरेक्ट करें
        return "redirect:/login?registered";
    }
}