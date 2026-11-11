# 🏢 Employee Management System (EMS)

A full-stack, enterprise-grade Employee Management System built using **Spring Boot 3**, **Spring Security 6**, **Spring Data JPA**, **Thymeleaf**, and **MySQL**.

---

## 🛠️ Tech Stack & Technologies Used

- **Backend:** Java 21, Spring Boot 3
- **Security:** Spring Security 6 (Role-Based Access Control, BCrypt Password Hashing)
- **Database:** MySQL 8.0+
- **ORM / Persistence:** Spring Data JPA, Hibernate 6
- **Template Engine:** Thymeleaf with Spring Security Extras
- **Architecture:** MVC (Model-View-Controller) + REST API Layer
- **Build Tool:** Apache Maven
- **Frontend / Styling:** Vanilla CSS3, Responsive Design, Modal Dialogs

---

## ✨ Key Features

1. **Role-Based Authentication & Authorization (RBAC):**
   - Database-backed authentication (`users`, `roles`, `users_roles` join table).
   - Password encryption using BCrypt.
   - User Registration (Sign-Up) with automatic `ROLE_USER` assignment.
   - Dynamic UI: Action buttons (Add, Edit, Delete) are visible only to `ADMIN`.
2. **Complete Employee Lifecycle (CRUD):**
   - Add, Edit, Delete (with POST safety), and View employee records.
   - Profile Photo upload with circular avatar display and initial fallbacks.
   - Relational Departments (`@ManyToOne`) with dropdown selection.
   - Active, On Leave, and Inactive status badges.
3. **Advanced Directory Controls:**
   - Server-side Pagination & Multi-column Dynamic Sorting.
   - Universal Case-Insensitive Search (across first name, last name, and department).
4. **Custom CSV Export:**
   - Interactive Modal Popup for filtered exports (Date range, Department, Status).
5. **REST API Layer (`/api/v1/employees`):**
   - Full CRUD endpoints returning JSON for integration with Mobile Apps or React/Angular.
   - Basic Auth support for Postman integration.

---

## 💻 Environment Setup & How to Run

### Prerequisites
Make sure you have installed:
- **Java Development Kit (JDK 21)**
- **MySQL Server (version 8.0 or above)**
- **Maven** (or use the included `./mvnw` wrapper)
- **Eclipse IDE / IntelliJ IDEA**

---

### Step 1: Database Setup
1. Open your MySQL client (Workbench or CLI) and create the database:
   ```sql
   CREATE DATABASE ems_db;