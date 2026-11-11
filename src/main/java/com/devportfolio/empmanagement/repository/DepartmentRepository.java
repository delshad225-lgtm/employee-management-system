package com.devportfolio.empmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devportfolio.empmanagement.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
	
	public Optional<Department> findByName(String name);

}
