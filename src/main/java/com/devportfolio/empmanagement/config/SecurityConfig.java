package com.devportfolio.empmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. PasswordEncoder: पासवर्ड को कभी भी प्लेन टेक्स्ट में नहीं रखा जाता, BCrypt से हैश (Encrypt) किया जाता है
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Users बनाना: अभी हम सीखने के लिए 2 इन-मेमोरी टेस्ट यूज़र्स बना रहे हैं
	/*
	 * @Bean public UserDetailsService userDetailsService(PasswordEncoder encoder) {
	 * UserDetails admin = User.builder() .username("admin")
	 * .password(encoder.encode("admin123")) // Encrypted Password .roles("ADMIN")
	 * .build();
	 * 
	 * UserDetails user = User.builder() .username("user")
	 * .password(encoder.encode("user123")) .roles("USER") .build();
	 * 
	 * return new InMemoryUserDetailsManager(admin, user); }
	 */

    // 3. Security Filter Chain: यही असली गार्ड है जो तय करता है कौन कहाँ जा सकता है
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        
     // 1. REST APIs के लिए CSRF को इग्नोर करें (ताकि Postman से POST/PUT/DELETE चल सके)
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
        
            .authorizeHttpRequests(auth -> auth
            	 // /login और /register को कोई भी बिना लॉगिन के खोल सकता है:
                 .requestMatchers("/login", "/register", "/css/**", "/js/**", "/uploads/**").permitAll()
                 
              // REST API रूल्स:
                 // GET APIs को USER और ADMIN दोनों एक्सेस कर सकते हैं:
                 .requestMatchers(HttpMethod.GET, "/api/v1/employees/**").hasAnyRole("ADMIN", "USER")
                 // POST, PUT, DELETE APIs सिर्फ ADMIN कर सकता है:
                 .requestMatchers(HttpMethod.POST, "/api/v1/employees/**").hasRole("ADMIN")
                 .requestMatchers(HttpMethod.PUT, "/api/v1/employees/**").hasRole("ADMIN")
                 .requestMatchers(HttpMethod.DELETE, "/api/v1/employees/**").hasRole("ADMIN")

                 // Web UI रूल्स:
                 
                 
                // सिर्फ ADMIN कर्मचारी को Add, Edit, और Delete कर सकता है
                .requestMatchers("/employees/new", "/employees/edit/**", "/employees/delete/**").hasRole("ADMIN")
                // लिस्ट देखना और CSV डाउनलोड करना ADMIN और USER दोनों कर सकते हैं
                .requestMatchers("/", "/employees", "/employees/export/**").hasAnyRole("ADMIN", "USER")
                // बाकी किसी भी रिक्वेस्ट के लिए लॉगिन होना ज़रूरी है
                .anyRequest().authenticated()
            )
            
         // 3. Postman और Mobile Apps के लिए HTTP Basic Authentication चालू करें:
            
            .httpBasic(Customizer.withDefaults()
            		
            )
         // 4. ब्राउज़र के लिए Form Login
            
            .formLogin(form -> form
            	.loginPage("/login")
                .defaultSuccessUrl("/employees", true) // लॉगिन होते ही सीधे /employees पर जाएगा
                .permitAll()
            )
            // लॉगआउट कॉन्फ़िगरेशन
            // लॉगआउट: लॉगआउट होने के बाद डिफ़ॉल्ट /login?logout पर भेजें और सेशन इनवैलिडेट करें
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}