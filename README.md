# Employee Management System (EMS)

Welcome to the **Employee Management System**, a comprehensive Enterprise Resource Planning (ERP) solution designed to streamline employee management, production tracking, and report generation.

---

## 🚀 Overview

This project is a full-stack web application featuring a robust **Spring Boot** backend and a dynamic **Angular** frontend. It provides tools for managing employees, tracking production stages, generating detailed reports (PDF/Excel), and handling secure user authentication.

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.2 (Java 17)
- **Security**: Spring Security with JWT (JSON Web Token)
- **Database**: MySQL 8.0+
- **ORM**: Spring Data JPA (Hibernate)
- **Reporting**: iText (PDF generation) & Apache POI (Excel generation)
- **Communication**: Spring Boot Mail (Email notifications)

### Frontend
- **Framework**: Angular 16.1.1
- **Styling**: Vanilla CSS & RxJS
- **API Communication**: HttpClient

---

## 📂 Project Structure

```text
EmployeeManagementSystem/
├── backend/            # Spring Boot application
│   ├── src/            # Java source files and resources
│   └── pom.xml         # Maven dependencies
├── frontend/           # Angular application
│   ├── src/            # Components, services, and assets
│   └── package.json    # Node.js dependencies
└── README.md           # Project documentation
```

---

## ⚙️ Getting Started

### Prerequisites
- **Java**: JDK 17
- **Maven**: 3.8+
- **Node.js**: 16.x or 18.x
- **Angular CLI**: 16.x
- **MySQL Server**: 8.0+

### Step 1: Backend Setup
1.  **Configure Database**:
    -   Open `backend/src/main/resources/application.properties`.
    -   Update `spring.datasource.url`, `username`, and `password` to match your local MySQL setup.
    -   The system will automatically create the `employee_managementsystem` database if it doesn't exist (`createDatabaseIfNotExist=true`).
2.  **Run Backend**:
    -   Navigate to the `backend` folder.
    -   Execute: `mvn spring-boot:run` (or use your IDE).
    -   The server starts at `http://localhost:8080`.

### Step 2: Frontend Setup
1.  **Install Dependencies**:
    -   Navigate to the `frontend` folder.
    -   Execute: `npm install`
2.  **Run Frontend**:
    -   Execute: `ng serve`
    -   The application will be available at `http://localhost:4200`.

---

## ✨ Key Features

- **🔐 Secure Authentication**: JWT-based login and role-based access control.
- **🏗️ Production Management**: Track production entries across multiple stages (Casting, Autoclave, Horizontal, etc.).
- **📊 Advanced Reporting**:
    - Export production data to **Excel** and **PDF**.
    - Plant-wise filtering for customized reports.
- **📧 Email Integration**: Automated notifications for system events (e.g., password resets).
- **📱 Responsive UI**: A modern interface optimized for desktop and mobile table views.

---

## 📝 License
This project is developed for internal business operations. For support or contributions, please contact the development team.
