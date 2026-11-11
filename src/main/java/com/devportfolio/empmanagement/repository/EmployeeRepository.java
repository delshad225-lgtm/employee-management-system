package com.devportfolio.empmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devportfolio.empmanagement.model.Employee;
import com.devportfolio.empmanagement.model.EmployeeStatus;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	// Basic CRUD operations are already inherited: save(), findById(), findAll(), deleteById(), etc.
	
	/* @Query("SELECT e FROM Employee e WHERE " +
	           "LOWER(e.fName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	           "LOWER(e.lName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	           "LOWER(e.department) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	    List<Employee> searchEmployees(@Param("keyword") String keyword);

}*/
	
	 
	// Added: Find employee by email
	    Optional<Employee> findByEmail(String email);
	    
	 // Pageable search across fName, lName, and department
	    // Spring Data derives both the WHERE clause AND handles mapped ORDER BY automatically
	 public   Page<Employee> findByFNameContainingIgnoreCaseOrLNameContainingIgnoreCaseOrDepartment_NameContainingIgnoreCase(
	            String fName, String lName, String deptName, Pageable pageable);
	 
	// नया मेथड: Date Range, Department और Status के आधार पर एक्सपोर्ट फ़िल्टर
	    @Query("SELECT e FROM Employee e WHERE " +
	           "(:startDate IS NULL OR e.joiningDate >= :startDate) AND " +
	           "(:endDate IS NULL OR e.joiningDate <= :endDate) AND " +
	           "(:deptId IS NULL OR e.department.id = :deptId) AND " +
	           "(:status IS NULL OR e.status = :status)")
	    List<Employee> filterEmployeesForExport(
	            @Param("startDate") LocalDate startDate,
	            @Param("endDate") LocalDate endDate,
	            @Param("deptId") Long deptId,
	            @Param("status") EmployeeStatus status);


}
