package com.devportfolio.empmanagement.service;

import java.util.List;

import com.devportfolio.empmanagement.model.Department;

public interface DepartmentService {
	
	List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    Department saveDepartment(Department department);

}
