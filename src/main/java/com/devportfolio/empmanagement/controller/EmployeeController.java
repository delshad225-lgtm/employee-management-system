package com.devportfolio.empmanagement.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.aspectj.weaver.reflect.Java14GenericSignatureInformationProvider;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devportfolio.empmanagement.model.Employee;
import com.devportfolio.empmanagement.model.EmployeeStatus;
import com.devportfolio.empmanagement.service.DepartmentService;
import com.devportfolio.empmanagement.service.EmployeeService;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@Controller
public class EmployeeController {
	
	private final EmployeeService empService;
	private final DepartmentService deptService;

	public EmployeeController(EmployeeService empService, DepartmentService deptService) {
		super();
		this.empService = empService;
		this.deptService = deptService;
	}
	
	
	  // Maps both root "/" and "/employees"
	  
	@GetMapping({"/","/employees"})
	public String viewHomePage(Model model,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "sortField", defaultValue = "fName") String sortField,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "keyword", required = false) String keyword) {
		int pageSize = 5; // Display 5 employees per page
		Page<Employee> page = empService.findPaginated(pageNo, pageSize, sortField, sortDir, keyword);
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("employees", page.getContent());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("keyword", keyword != null ? keyword : "");
		// Export Modal के ड्रॉपडाउन के लिए डेटा
		model.addAttribute("departments", deptService.getAllDepartments());
		model.addAttribute("statuses", Arrays.asList(EmployeeStatus.values()));
		
		return "employees";
	}
	 
	
	// 1. Display the empty form to the user
	@GetMapping("/employees/new")
	public String showFormToAddEmp(Model model) {
		// We pass a blank Employee so Thymeleaf can bind form inputs to its properties
		Employee emp=new Employee();
		model.addAttribute("employee",emp);
		model.addAttribute("departments", deptService.getAllDepartments());
		model.addAttribute("statuses", Arrays.asList(EmployeeStatus.values()));
		return "add-employee";
	}
	
	@PostMapping("/employees")
	public String saveEmp(@Valid @ModelAttribute("employee") Employee employee, BindingResult result,@RequestParam("imageFile") MultipartFile imageFile,Model model,
			RedirectAttributes redirectAttributes) {
		
		//Custom validation checkemail uniqueness for new employee
		
		if(employee.getEmail()!=null && !empService.isEmailUnique(null, employee.getEmail())) {
			result.addError(new FieldError("employee", "email", "this email is already in use by another employee"));
		}
		
		if(result.hasErrors()) {
			model.addAttribute("departments", deptService.getAllDepartments());
			model.addAttribute("statuses", Arrays.asList(EmployeeStatus.values()));
			System.out.println("=============has error==============");
			return "add-employee";
		}
		
		// फोटो अपलोड प्रोसेस करें
				if (!imageFile.isEmpty()) {
					try {
						String uploadDir = "uploads/";
						Path uploadPath = Paths.get(uploadDir);
						System.out.println("========== "+uploadPath+" ==============");
						if (!Files.exists(uploadPath)) {
							Files.createDirectories(uploadPath); // फोल्डर नहीं है तो बनाओ
						}
						
						// फाइल का यूनिक नाम बनाएँ (ताकि दो जॉन डो की फोटो ओवरराइट न हो)
						String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
						Path filePath = uploadPath.resolve(fileName);
						
						try (InputStream inputStream = imageFile.getInputStream()) {
							Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
						}
						employee.setProfileImage(fileName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		
		empService.saveEmployee(employee);
		
		// Flash message for adding
		redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully!");
		
		// Use PRG pattern: Redirect back to the employee list page
		return "redirect:/employees";
	}
	
	
	@GetMapping("/employees/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		
		Employee employeeById = empService.getEmployeeById(id);
		System.out.println("Fetched employee: " + employeeById);
		model.addAttribute("employeeById", employeeById);
		model.addAttribute("departments", deptService.getAllDepartments());
		model.addAttribute("statuses", Arrays.asList(EmployeeStatus.values()));
		
		return "edit-employee";
	}
	
	@PostMapping("/employees/{id}")
	public String updateEmployee(@PathVariable("id") Long id, @Valid @ModelAttribute("employeeById") Employee employee,BindingResult result,
			 @RequestParam("imageFile") MultipartFile imageFile, Model model,RedirectAttributes redirectAttributes) {
		
		if(employee.getEmail()!=null && !empService.isEmailUnique(id,employee.getEmail())) {
			result.addError(new FieldError("employee", "email", "this email is already in use by another employee"));
		}
		
		if(result.hasErrors()) {
			employee.setId(id);
			model.addAttribute("departments", deptService.getAllDepartments());
			model.addAttribute("statuses", Arrays.asList(EmployeeStatus.values()));
			return "edit-employee";
		}
		
		employee.setId(id);
		// अगर नई फोटो अपलोड की गई है
				if (!imageFile.isEmpty()) {
					try {
						String uploadDir = "uploads/";
						Path uploadPath = Paths.get(uploadDir);
						System.out.println("=========="+uploadPath+"=============");
						if (!Files.exists(uploadPath)) {
							Files.createDirectories(uploadPath);
							System.out.println("=========="+Files.createDirectories(uploadPath)+"=============");
						}
						
						String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
						Path filePath = uploadPath.resolve(fileName);
						System.out.println("=========="+filePath+"=============");
						try (InputStream inputStream = imageFile.getInputStream()) {
							Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
						}
						employee.setProfileImage(fileName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// अगर यूजर ने नई फोटो नहीं चुनी, तो पुरानी वाली फोटो बरकरार रखें
					Employee existingEmp = empService.getEmployeeById(id);
					employee.setProfileImage(existingEmp.getProfileImage());
				}
		empService.saveEmployee(employee);
		
		// Flash message for updating
		redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
		return "redirect:/employees";
	}
	
	// get mapping never used for delete operation in production
	
	/*
	 * @GetMapping("/employees/delete/{id}") public String
	 * deleteEmployee(@PathVariable("id") Long id) {
	 * empService.deleteEmployeeById(id); return "redirect:/employees"; }
	 */
	
	@PostMapping("/employees/delete/{id}")
	public String deleteEmp(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		empService.deleteEmployeeById(id);
		// Flash message for deleting
		redirectAttributes.addFlashAttribute("dangerMessage", "Employee deleted successfully!");
		return "redirect:/employees";
	}
	
	@GetMapping("/employees/export/csv")
	public void exportToCSV(@RequestParam(value = "startDate", required = false)
	                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			                @RequestParam(value = "endDate", required = false) 
	                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			                @RequestParam(value = "departmentId", required = false) Long departmentId,
			                @RequestParam(value = "status", required = false) EmployeeStatus status,
			                HttpServletResponse response) throws IOException {
		
		//setting http response header and content type
		
		response.setContentType("text/csv");
		String headerKey="Content-Disposition";
		String headerValue="attachment; filename=employees_"+java.time.LocalDate.now() + ".csv";
		response.setHeader(headerKey, headerValue);
		
		List<Employee> allEmployees = empService.getEmployeesForExport(startDate, endDate, departmentId, status);
		
		//to write csv using response writer
		PrintWriter writer = response.getWriter();
		
		//name of csv header
		
		writer.println("ID,First Name,Last Name,Email,Department,Salary,Joining Date,Status");
		
		// writting every employee data in one single line
		for(Employee emp : allEmployees) {
			
			String deptName=(emp.getDepartment()!=null)? emp.getDepartment().getName() : "N/A";
			String joiningDate=(emp.getJoiningDate()!=null) ? emp.getJoiningDate().toString() : "N/A";
			String empStatus = (emp.getStatus() != null) ? emp.getStatus().name() : "N/A";
			writer.println(String.format("%d,%s,%s,%s,%s,%.2f,%s,%s", 
					                      emp.getId(),
					                      escapeCsv(emp.getfName()),
					                      escapeCsv(emp.getlName()),
					                      escapeCsv(emp.getEmail()),
					                      escapeCsv(deptName),
					                      emp.getSalary(),
					                      joiningDate,
					                      empStatus
					                      ));
		}
	}
	
	//escape csv helper method to remove commos from name
	
	private String escapeCsv(String data) {
		if (data == null) return "";
		if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
			return "\"" + data.replace("\"", "\"\"") + "\"";
		}
		return data;
	}
	
	
	
	
	

}
