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
- **Language**: Java 25  
- **Dependency manager**: Maven  
- **Database**: MariaDB  
- **Tools**: JDBC, Spring Boot (later), JPA/Hibernate  

## Initial Database
The main tables are:  
- `books` (book information)  
- `authors` (authors information)  
- `users` (registered users)  
- `loans` (book loans tracking)  
- `categories` (book classification)  
- `copies` (physical book copies inventory)
- `book_author` (N:M relationship between books and authors)  
- `passwords` (credential management with hashing)

## Key Relationships
- **Books ↔ Authors**: Many-to-many via `book_author` junction table
- **Books ↔ Categories**: Many-to-one (each book belongs to one category)
- **Books ↔ Copies**: One-to-many (each book can have multiple physical copies)
- **Copies ↔ Loans**: One-to-many (each copy can have multiple loan records over time)
- **Users ↔ Loans**: One-to-many (each user can have multiple active loans)  

## Architecture Decisions
- **Why JDBC first?** → Understand low-level database operations
- **Why MariaDB?** → Lightweight, production-ready
- **Layered architecture** → Separation of concerns

## Project Structure

Update Note:
The project structure has been reorganized to improve modularity and maintainability.
DAO implementations are now separated under dao/impl, and a new service layer has been introduced to handle business logic and validation before DAO interaction.

```bash
📦 AlexandriaLMS
├── 📂 src
│   ├── 📂 com
│   │   └── 📂 alexandrialms
│   │       ├── 📂 dao
│   │       │   ├── 📂 impl                     # NEW: DAO implementations
│   │       │   │   ├── AuthorDAO.java
│   │       │   │   ├── BookDAO.java
│   │       │   │   └── (other DAO implementations)
│   │       │   └── 📂 interfaces
│   │       │       ├── AuthorDAOInterface.java
│   │       │       ├── BookDAOInterface.java
│   │       │       ├── GenericDAO.java
│   │       │       └── (other DAO interfaces)
│   │       │    
│   │       │
│   │       ├── 📂 model
│   │       │   ├── Author.java
│   │       │   ├── Book.java
│   │       │   └── (other entity classes)
│   │       │
│   │       ├── 📂 service                     # NEW: Service Layer
│   │       │   ├── 📂 interfaces
│   │       │   │   ├── AuthorServiceInterface.java
│   │       │   │   ├── BookServiceInterface.java
│   │       │   │   └── (future service interfaces)
│   │       │   ├── 📂 impl
│   │       │   │   ├── AuthorServiceImpl.java
│   │       │   │   └── (future service implementations)
│   │       │
│   │       ├── 📂 util
│   │       │   ├── ValidationHelper.java
│   │       │   ├── ValidationException.java # NEW: custom exception
│   │       │   └── DBConnection.java    
│   │       │
│   │       └── 📂 test
│   │           ├── ValidationHelperTest.java
│   │           └── (other test classes)
│   │
│   └── 📂 resources
│       └── (SQL scripts, config files, etc.)
│
├── README.md
├── CHANGELOG.md
└── .gitignore
```

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/ADelgadoMontoro/AlexandriaLMS.git
2. Configure the database in src/main/resources/application.properties
3. Build with Maven: mvn clean install
4. Run: mvn exec:java

## Author

**Adrián Delgado Montoro**  
[adelgadomontoro@gmail.com](mailto:adelgadomontoro@gmail.com)  
[GitHub: ADelgadoMontoro](https://github.com/ADelgadoMontoro)
