package com.devportfolio.empmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devportfolio.empmanagement.dto.AuthRequest;
import com.devportfolio.empmanagement.dto.AuthResponse;
import com.devportfolio.empmanagement.security.JwtUtils;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public AuthRestController(AuthenticationManager authenticationManager,
                              UserDetailsService userDetailsService,
                              JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginForToken(@RequestBody AuthRequest authRequest) {
        try {
            // 1. क्रेडेंशियल्स की जांच (अगर गलत हुआ तो सीधे catch ब्लॉक में जाएगा)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // ❌ गलत यूज़रनेम या पासवर्ड होने पर: HTTP 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: Invalid username or password!");
        } catch (DisabledException e) {
            // ❌ अगर यूज़र का अकाउंट डिसेबल/ब्लॉक है
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error: User account is disabled!");
        }

        // 2. अगर पासवर्ड 100% सही था, तभी यह लाइन चलेगी:
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // 3. JWT टोकन जनरेट करके 200 OK में लौटाएँ
        final String jwt = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}