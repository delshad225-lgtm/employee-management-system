package com.devportfolio.empmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devportfolio.empmanagement.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	public Optional<Role> findByName(String name);

}
