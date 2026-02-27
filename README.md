RevShop – E-Commerce Web Application
RevShop is a full-stack E-Commerce web application built using Spring Boot (Backend) and Angular (Frontend).
It supports authentication, product management, cart system, order processing, and secure JWT-based authorization.

🚀 Tech Stack

🔹 Backend

Java 21

Spring Boot

Spring Security

JWT Authentication

Hibernate / JPA

MySQL

Maven

🔹 Frontend

Angular

TypeScript

Bootstrap

RxJS


📦 Features
👤 Authentication & Authorization

User Registration

Secure Login

JWT-based Authentication

Role-based access control (Admin / User)

🛍 Product Management

Add Products (Admin)

Update Products

Delete Products

Upload Product Images

Category-based filtering

🛒 Cart System

Add to cart

Update quantity

Remove from cart

View total price

📦 Order Management

Place Order

View Order History

Stock management

Low stock threshold tracking

🏗 Project Architecture

revshop
 ├── config
 ├── controller
 ├── service
 ├── repository
 ├── security
 ├── dto
 ├── exception
 └── model

Architecture follows layered pattern:

Controller → Service → Repository → Database

⚙️ Installation & Setup Guide

1️⃣ Clone the Repository
git clone https://github.com/your-username/revshop.git
cd revshop

2️⃣ Backend Setup (Spring Boot)

🛠 Configure Database

Create a MySQL database:

CREATE DATABASE revshop;

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/revshop
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

▶️ Run Backend

Using Maven:

mvn clean install
mvn spring-boot:run

OR

Run from IDE:

Open in IntelliJ / Eclipse

Run RevShopApplication.java

Backend runs at:

http://localhost:8080

🔹 3️⃣ Frontend Setup (Angular)

Navigate to frontend folder:

cd revshop-ui
npm install
ng serve

Frontend runs at:

http://localhost:4200
🔐 Default Roles
Role	Access
USER	Shop, Cart, Orders
ADMIN	Product Management, Inventory

🧪 API Testing (Postman)

Import provided Postman collection.

Example APIs:

Register:

POST /api/auth/register

Login:

POST /api/auth/login

Add Product:

POST /api/products

Add to Cart:

POST /api/cart
🛡 Security

Password encrypted using BCrypt

JWT token validation

Role-based method authorization

Exception handling with global handler

📊 Future Improvements

Payment Gateway Integration

Order Tracking

Email Notifications

Microservices Migration

CI/CD automation

🧑‍💻 Author

Developed by Ajay Kumar
Java Full Stack Developer
