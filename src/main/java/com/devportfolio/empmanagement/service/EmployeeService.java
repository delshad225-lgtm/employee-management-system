package com.devportfolio.empmanagement.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.devportfolio.empmanagement.model.Employee;
import com.devportfolio.empmanagement.model.EmployeeStatus;

public interface EmployeeService {
	
	public List<Employee> getAllEmployees();
	
	public void saveEmployee(Employee employee);
	
	public Employee getEmployeeById(Long id);
	
	public void deleteEmployeeById(Long id);
	
	/* public List<Employee> getAllEmployees(String keyword); */
	
	// Added for Pagination & Sorting
	public	Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection, String keyword);
	
	// Added: Check if email is unique (id can be null for new employee)
	boolean isEmailUnique(Long id, String email);
		
	public List<Employee> getEmployeesForExport(java.time.LocalDate startDate, java.time.LocalDate endDate, Long deptId, EmployeeStatus status);

}
