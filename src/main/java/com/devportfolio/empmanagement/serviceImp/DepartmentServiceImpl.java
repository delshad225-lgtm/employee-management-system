package com.devportfolio.empmanagement.serviceImp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devportfolio.empmanagement.exception.ResourceNotFoundException;
import com.devportfolio.empmanagement.model.Department;
import com.devportfolio.empmanagement.repository.DepartmentRepository;
import com.devportfolio.empmanagement.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	
	private final DepartmentRepository deptRepo;
	
	

	public DepartmentServiceImpl(DepartmentRepository deptRepo) {
		super();
		this.deptRepo = deptRepo;
	}

	@Override
	public List<Department> getAllDepartments() {
		// TODO Auto-generated method stub
		return deptRepo.findAll();
	}

	@Override
	public Department getDepartmentById(Long id) {
		// TODO Auto-generated method stub
		return deptRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Department not found with id "+id));
	}

	@Override
	public Department saveDepartment(Department department) {
		// TODO Auto-generated method stub
		return deptRepo.save(department);
	}

}
