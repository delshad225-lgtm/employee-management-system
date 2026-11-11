package com.devportfolio.empmanagement.serviceImp;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.devportfolio.empmanagement.exception.ResourceNotFoundException;
import com.devportfolio.empmanagement.model.Employee;
import com.devportfolio.empmanagement.model.EmployeeStatus;
import com.devportfolio.empmanagement.repository.EmployeeRepository;
import com.devportfolio.empmanagement.service.EmployeeService;


@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	private EmployeeRepository empRepo;
	
	

	public EmployeeServiceImpl(EmployeeRepository empRepo) {
		super();
		this.empRepo = empRepo;
	}



	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> allEmployees = empRepo.findAll();
		return allEmployees;
	}



	@Override
	public void saveEmployee(Employee employee) {
		Employee save = empRepo.save(employee);
		
	}



	@Override
	public Employee getEmployeeById(Long id) {
		
		return empRepo.findById(id)
		.orElseThrow(()->new ResourceNotFoundException("Employee not found for id "+id));
		
	}



	@Override
	public void deleteEmployeeById(Long id) {
		empRepo.deleteById(id);
		
	}



	/*
	 * @Override public Page<Employee> findPaginated(int pageNo, int pageSize,
	 * String sortField, String sortDirection, String keyword) { Sort sort =
	 * sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
	 * Sort.by(sortField).ascending() : Sort.by(sortField).descending();
	 * 
	 * // Pageable is 0-indexed in Spring Data Pageable pageable =
	 * PageRequest.of(pageNo - 1, pageSize, sort);
	 * 
	 * if (keyword != null && !keyword.trim().isEmpty()) { return
	 * empRepo.searchEmployees(keyword.trim(), pageable); } return
	 * empRepo.findAll(pageable); }
	 */



	@Override
	public boolean isEmailUnique(Long id, String email) {
		Optional<Employee> existingEmp = empRepo.findByEmail(email);
		
		if(existingEmp.isEmpty()) {
			
			return true; // No Record available in db with this email
		}
		
		// if creating new employee with existing email in db
		
		if(id==null) {
			return false; // that means crating new employee with exiting email in db
		}
		
		// checking that is existing employee updating his deatails with his own unique email or not
		boolean result = existingEmp.get().getId().equals(id);
		return result;
	}
	
	
	@Override
	public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection, String keyword) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) 
				? Sort.by(sortField).ascending() 
				: Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		
		if (keyword != null && !keyword.trim().isEmpty()) {
			String cleanKeyword = keyword.trim();
			return empRepo.findByFNameContainingIgnoreCaseOrLNameContainingIgnoreCaseOrDepartment_NameContainingIgnoreCase(
					cleanKeyword, cleanKeyword, cleanKeyword, pageable);
		}
		return empRepo.findAll(pageable);
	}



	@Override
	public List<Employee> getEmployeesForExport(LocalDate startDate, LocalDate endDate, Long deptId,
			EmployeeStatus status) {
		// TODO Auto-generated method stub
		return empRepo.filterEmployeesForExport(startDate, endDate, deptId, status);
	}




	/*
	 * @Override public List<Employee> getAllEmployees(String keyword) {
	 * 
	 * if(keyword!=null && !keyword.trim().isEmpty()) { return
	 * empRepo.searchEmployees(keyword.trim()); }
	 * 
	 * 
	 * return empRepo.findAll() ; }
	 */

}

