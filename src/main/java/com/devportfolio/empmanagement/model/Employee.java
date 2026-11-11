package com.devportfolio.empmanagement.model;

import java.time.LocalDate;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name="employee")
public class Employee {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="First_Name", nullable = false)
	@NotBlank(message = "First Name is required")
	private String fName;
	
	@Column(name="Last_Name", nullable = false)
	@NotBlank(message = "Last Name is required")
	private String lName;
	
	@Column(name = "Email" , nullable = false, unique = true)
	@NotBlank(message = "Email is required")
	@Email(message = "Please Provide a valid email Address")
	private String email;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id")
	//@NotFound(action = NotFoundAction.IGNORE) // <-- Prevents crashing if an old ID (like 0) is missing
	@NotNull(message = "Department is required")
	private Department department;
	
	@Column(name = "Salary", nullable = false)
	@NotNull(message = "Salary is required")
	@Positive(message = "Salary must be greater than zero")
	private Double salary;
	
	// नई फील्ड: Joining Date (भविष्य की तारीख नहीं हो सकती)
	
	@Column(name="joining_date", nullable=false)
	@NotNull(message = "Joining date is required")
	@PastOrPresent(message = "Joining date cant be in future")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate joiningDate;
	
	// नई फील्ड: Status (ACTIVE / ON_LEAVE / INACTIVE)
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@NotNull(message = "Status is required")
	private EmployeeStatus status = EmployeeStatus.ACTIVE;
	
	// नई फील्ड: फोटो का नाम स्टोर करने के लिए
		@Column(name = "profile_image")
		private String profileImage;

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(String fName, String lName, String email, Department department, Double salary,
			LocalDate joiningDate, EmployeeStatus status,String profileImage) {
		super();
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.department = department;
		this.salary = salary;
		this.joiningDate = joiningDate;
		this.status = status;
		/* this.profileImage = profileImage; */
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public EmployeeStatus getStatus() {
		return status;
	}

	public void setStatus(EmployeeStatus status) {
		this.status = status;
	}
	
	// Getter और Setter
		public String getProfileImage() {
			return profileImage;
		}

		public void setProfileImage(String profileImage) {
			this.profileImage = profileImage;
		}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", fName=" + fName + ", lName=" + lName + ", email=" + email + ", department="
				+ department + ", salary=" + salary + ", joiningDate=" + joiningDate + ", status=" + status + "]";
	}

	

	
	
	
	
	
}
