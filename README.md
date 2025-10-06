# AlexandriaLMS

Alexandria is a library management system developed in **Java** with **Maven** and **MariaDB** as the database.  
The project is designed as a progressive learning experience in several phases:

## Version Roadmap
- **Version 0**: Environment setup (repository, database, dependencies) 
- **Version 1**: Database access with JDBC + DAO.
- **Version 2**: REST API creation with Spring Boot.  
- **Version 3**: Business logic features (loans, returns, rules).  
- **Version 4**: Quality improvements (validations, error handling, tests).  
- **Version 5**: Graphical interface (JavaFX or web frontend).  
- **Version 6**: Extras (security, exports, notifications).  

## Technologies
- **Language**: Java 17+  
- **Dependency manager**: Maven  
- **Database**: MariaDB  
- **Tools**: JDBC, Spring Boot (later), JPA/Hibernate  

## Initial Database
The main tables are:  
- `Book` (book information)  
- `Author` (authors)  
- `User` (registered users)  
- `Loan` (book loans)  
- `Category` (book classification)  
- `Book_Author` (N:M relationship between books and authors)  
- `Passwords` (credential management with hashing)  

## Project Structure
```bash
alexandria/
├── src/
│   ├── main/java/ # Source code
│   ├── main/resources/ # Configuration
│   └── test/ # Tests
├── pom.xml # Maven configuration
├── README.md
└── .gitignore
```

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/adelgadomontoro/alexandria.git
2. Configure the database in src/main/resources/application.properties
3. Build with Maven: mvn clean install
4. Run: mvn exec:java

## Author

**Adrián Delgado Montoro**  
[adelgadomontoro@gmail.com](mailto:adelgadomontoro@gmail.com)  
[GitHub: ADelgadoMontoro](https://github.com/ADelgadoMontoro)
