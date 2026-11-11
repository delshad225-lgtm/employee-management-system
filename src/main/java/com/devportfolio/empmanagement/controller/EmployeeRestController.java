package com.devportfolio.empmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devportfolio.empmanagement.dto.EmployeeRequestDto;
import com.devportfolio.empmanagement.model.Department;
import com.devportfolio.empmanagement.model.Employee;
import com.devportfolio.empmanagement.service.DepartmentService;
import com.devportfolio.empmanagement.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeRestController {

    private final EmployeeService empService;
    private final DepartmentService deptService;

    public EmployeeRestController(EmployeeService empService, DepartmentService deptService) {
        this.empService = empService;
        this.deptService = deptService;
    }

    // 1. GET ALL: सारे कर्मचारी JSON लिस्ट में प्राप्त करें (ऑप्शनल सर्च कीवर्ड के साथ)
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam(value = "keyword", required = false) String keyword) {
        List<Employee> employees = empService.getAllEmployees();
        return ResponseEntity.ok(employees); // HTTP 200 OK
    }

    // 2. GET BY ID: किसी एक कर्मचारी का डेटा निकालें
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        Employee employee = empService.getEmployeeById(id);
        return ResponseEntity.ok(employee); // HTTP 200 OK
    }

    // 3. POST (CREATE): नया कर्मचारी बनाएँ
    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeRequestDto dto) {
        // Email Uniqueness चेक करें
        if (!empService.isEmailUnique(null, dto.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use by another employee");
        }

        Department department = deptService.getDepartmentById(dto.getDepartmentId());

        Employee employee = new Employee();
        employee.setfName(dto.getfName());
        employee.setlName(dto.getlName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());
        employee.setDepartment(department);
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setStatus(dto.getStatus());

        empService.saveEmployee(employee);

        return new ResponseEntity<>(employee, HttpStatus.CREATED); // HTTP 201 Created
    }

    // 4. PUT (UPDATE): मौजूदा कर्मचारी को अपडेट करें
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") Long id, @Valid @RequestBody EmployeeRequestDto dto) {
        // चेक करें कि कर्मचारी मौजूद है या नहीं
        Employee existingEmployee = empService.getEmployeeById(id);

        // Email Uniqueness चेक करें (खुद के अलावा किसी और का ईमेल न हो)
        if (!empService.isEmailUnique(id, dto.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use by another employee");
        }

        Department department = deptService.getDepartmentById(dto.getDepartmentId());

        existingEmployee.setfName(dto.getfName());
        existingEmployee.setlName(dto.getlName());
        existingEmployee.setEmail(dto.getEmail());
        existingEmployee.setSalary(dto.getSalary());
        existingEmployee.setDepartment(department);
        existingEmployee.setJoiningDate(dto.getJoiningDate());
        existingEmployee.setStatus(dto.getStatus());

        empService.saveEmployee(existingEmployee);

        return ResponseEntity.ok(existingEmployee); // HTTP 200 OK
    }

    // 5. DELETE: कर्मचारी को डिलीट करें
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        // चेक करें कि रिकॉर्ड है या नहीं (अगर नहीं होगा तो Exception Handler 404 फेंक देगा)
        empService.getEmployeeById(id);
        empService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}