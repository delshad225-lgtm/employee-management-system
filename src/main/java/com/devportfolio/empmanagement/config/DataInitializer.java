package com.devportfolio.empmanagement.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.devportfolio.empmanagement.model.Department;
import com.devportfolio.empmanagement.model.Role;
import com.devportfolio.empmanagement.model.User;
import com.devportfolio.empmanagement.repository.DepartmentRepository;
import com.devportfolio.empmanagement.repository.RoleRepository;
import com.devportfolio.empmanagement.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
	
	private final DepartmentRepository deptRepo;
	
	private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
	
	
	public DataInitializer(DepartmentRepository deptRepo, RoleRepository roleRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.deptRepo = deptRepo;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}







	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		if(deptRepo.count()==0) {
			List<Department> defaultDepartments = Arrays.asList(new Department("Account"),
					      new Department("Finance"),
					      new Department("Logistic"),
					      new Department("Engineering"),
					      new Department("Marketing"),
					      new Department("Operation"),
					      new Department("Human Resource")
					      );
			deptRepo.saveAll(defaultDepartments);
			 System.out.println(">>> Initialized default departments in database.");
		}
		
		 // 2. Roles इनिशियलाइज़ करें
		Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        // 3. Admin User बनाएँ (अगर नहीं है)
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // BCrypt encrypted
            admin.setEnabled(true);
            admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
            userRepository.save(admin);
            System.out.println(">>> Initialized Admin user in database.");
        }

        // 4. Regular User बनाएँ (अगर नहीं है)
        if (userRepository.findByUsername("user").isEmpty()) {
            User normalUser = new User();
            normalUser.setUsername("user");
            normalUser.setPassword(passwordEncoder.encode("user123")); // BCrypt encrypted
            normalUser.setEnabled(true);
            normalUser.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            userRepository.save(normalUser);
            System.out.println(">>> Initialized Regular user in database.");
        }
		
	}

}
