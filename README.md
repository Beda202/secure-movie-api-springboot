# 🔐 Secure REST API - Spring Boot Backend

A complete Backend REST API built using Spring Boot.
This project demonstrates advanced backend concepts including JWT Authentication, File Handling, Pagination, Exception Handling, and Password Reset functionality.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT (JSON Web Token)
- MySQL
- Maven
- Lombok
- Postman (API Testing)

---

## 📌 Core Features

### 🔐 Authentication & Security
- User Registration
- User Login
- JWT Authentication
- Role-Based Authorization
- Secure API Access
- Forgot Password
- Reset Password via Token

### 📂 File Handling
- Upload Files
- Download Files
- Store Files on Server
- File Validation

### 🗂️ CRUD Operations
- Create, Read, Update, Delete APIs
- DTO Pattern
- Validation using Annotations

### 📊 Pagination & Sorting
- Dynamic Pagination
- Sorting by Fields (ASC/DESC)

### ⚠️ Exception Handling
- Global Exception Handler
- Custom Exceptions
- Proper HTTP Status Codes

### 🧪 API Testing
- Tested using Postman
- Secured Endpoints with JWT Token

---

## 🗄️ Database Structure

Entities may include:
- User
- Role
- File
- Any domain entity based on your implementation

Relationships:
- One User can have multiple roles
- One User can upload multiple files

---

## 🔑 Authentication Flow

1. User registers
2. User logs in
3. JWT token is generated
4. Token must be included in Authorization Header:
   
   Authorization: Bearer <token>

5. Secured endpoints validate the token

---

## 📡 Sample Endpoints

### 🔐 Auth APIs

POST /api/auth/register  
POST /api/auth/login  
POST /api/auth/forgot-password  
POST /api/auth/reset-password  

### 📂 File APIs

POST /api/files/upload  
GET /api/files/download/{filename}  

### 📦 Example Entity CRUD

GET /api/items?page=0&size=5&sortBy=name  
POST /api/items  
PUT /api/items/{id}  
DELETE /api/items/{id}  

---

## ⚙️ How to Run the Project

1. Clone the repository:
   git clone https://github.com/your-username/project-name.git

2. Open in IntelliJ IDEA

3. Configure MySQL database in:
   src/main/resources/application.properties

4. Run the application

5. Test APIs using Postman

---

## 🔮 Future Improvements

- Add Swagger Documentation
- Dockerize the application
- Deploy on AWS
- Add Email Service for Password Reset
- Add Refresh Token mechanism

---

## 🎯 What This Project Demonstrates

✔ Strong understanding of Spring Boot  
✔ Secure API development  
✔ Clean Architecture (Controller - Service - Repository)  
✔ Production-ready authentication system  
✔ Real-world backend development skills  

---

## 👨‍💻 Author

Your Name  
Junior Backend Developer  
Java | Spring Boot | REST APIs
