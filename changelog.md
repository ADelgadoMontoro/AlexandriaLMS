# Changelog — AlexandriaLMS
## [v0.9.0] - 2025-01-20
### Added
- **Complete User Management System:**
  - `UserDAOImpl` with 25+ data access methods
  - `UserServiceInterface` with 45+ business operations
  - `UserServiceImpl` complete implementation
  - `UserServiceIntegrationTest` with 22 passing tests

- **Complete Copy Management System:**
  - `CopyDAOInterface` with comprehensive inventory operations
  - `CopyServiceInterface` with availability management
  - `CopyServiceImpl` complete implementation
  - Copy entity model with status management
  - Copy validation methods in `ValidationHelper`

- **Enhanced User Features:**
  - Role-based loan limits (`LibraryRole` enum with loan limits)
  - Advanced user search and filtering capabilities
  - Comprehensive user statistics and reporting
  - User eligibility validation for loans
  - Pagination support for user listings

- **Advanced Validation System:**
  - User-specific validations in `ValidationHelper`
  - Copy-specific validations (status, availability, uniqueness)
  - Email and phone format validation
  - Date range validation for registration periods
  - User and copy existence checks

### Changed
- **Architecture Refinements:**
  - Enhanced DAO method consistency across implementations
  - Improved service layer error handling patterns

- **Validation Patterns:**
  - Unified validation approach for user and copy operations
  - Consistent exception handling across all services
  - Enhanced business rule enforcement

### Technical Debt
- **Pending Implementations:**
  - `LoanDAO` and `LoanService` implementations
  - `PasswordDAO` and `PasswordService` implementations
  - Integration between User, Copy and Loan services

### Notes
- **User and Copy modules completed** with comprehensive implementations
- **Ready for Loan service implementation** as final core entity
- **Foundation established** for complete library loan workflow
- **All user service integration tests passing** (22/22)

## [v0.8.0] - 2025-10-19
### Added
- **Complete Service Layer Implementation:**
  - `BookServiceImpl` with 45+ business methods
  - `AuthorServiceImpl` with comprehensive author management
  - `CategoryServiceImpl` with 19 category operations
  - Complete service interfaces for all entities
- **Advanced Validation System:**
  - `ValidationHelper` expanded with book, author, and category validations
  - `validateBookForInsert()`, `validateBookForUpdate()`, `validateCategoryForInsert()`, etc.
  - Fail-fast validation pattern across all services
- **Comprehensive Integration Testing:**
  - `BookServiceIntegrationTest`: 15 passing tests
  - `AuthorServiceIntegrationTest`: 18 passing tests  
  - `CategoryServiceIntegrationTest`: 24 passing tests
  - Total: 57 integration tests covering CRUD, search, statistics, and business logic
- **Enhanced DAO Layer:**
  - `CategoryDAOInterface` with 13 advanced methods
  - Complete `CategoryDAOImpl` implementation
  - Advanced search, statistics, and maintenance operations

### Changed
- **Architecture Refinement:**
  - Solidified layered architecture: Model → DAO → Service → Tests
  - Consistent error handling with `ValidationException`
  - Proper use of `Optional` for "not found" scenarios
- **Validation Patterns:**
  - Unified validation approach across all services
  - Separation of business validation vs data access

### Notes
- **Core business services implemented** with comprehensive testing
- **Solid architectural foundation** established for remaining entities
- **Testing patterns defined** for easy extension to Copy, Loan, User, and Password services

## [v0.5.0] - 2025-10-06  
### Added
- Created `service` layer with:
  - `AuthorServiceInterface` and `BookServiceInterface`
  - `AuthorServiceImpl` implementation
- Added `ValidationException` class for centralized validation error handling
- Introduced new author validation methods in `ValidationHelper`
- Added several new query and utility methods to `AuthorDAOInterface` and `AuthorDAOImpl`

### Changed
- Reorganized DAO package structure:
  - Implementations moved to `dao/impl`
  - Interfaces kept in `dao/interfaces`
- Updated project structure documentation in `README.md` to reflect new organization

### Notes
- This update introduces a clear separation between **data access (DAO)** and **business logic (Service)** layers.  
- The project is now closer to a layered architecture model, preparing the groundwork for future integration with Spring.

## [v0.2.0] - 2025-10-06

### Added
- **GenericDAO Interface:**  
  Introduced a generic DAO interface defining CRUD operations and a `mapResultSet` method for entity mapping.
- **Specific DAO Interfaces:**  
  Created interfaces for `Author`, `Book`, `Category`, `Copy`, `Loan`, `Password`, and `User` DAOs.
- **ValidationHelper:**  
  Implemented multiple validation methods:
  - `isValidISBN()` and control digit validation  
  - `isValidCategoryID()` and `isValidUserID()`  
  - `isValidEmail()`, `isValidPhone()`, and `isValidDateRange()`  
- **ValidationHelperTest:**  
  Added JUnit test suite to verify validator correctness.
- **DBConnection update:**  
  Refactored to load database configuration from an external `db.properties` file.
- **New DAO Methods:**  
  Added query and helper methods such as `findActiveLoans()`, `findByUser()`, `findByLastName()`, and `getPasswordHistory()`.

### Changed
- Updated all DAOs to implement their corresponding interfaces.
- Refactored code to improve modularity and dependency handling.
- Updated **README.md** with clearer structure and package explanations.

### Fixed
- Corrected issues with ISBN regex handling and null-safe checks in validators.
- Improved error handling in database connection setup.

### Documentation
- Added detailed package structure section to `README.md`.
---

**Next planned tasks**
- Add `ValidationException` class.  
- Expand DAO logic with full CRUD validation before operations.  
- Add `Book-Author` view implementation.
- Implement validation methods (e.g., isValidBookToOperate()) to ensure entity integrity before database operations such as insert or update.

## [0.1.0] - 2025-10-02
### Added
- Initial DAOs for all entities: `BookDAO`, `AuthorDAO`, `CategoryDAO`, `UserDAO`, `LoanDAO`, `PasswordDAO`.
- Model classes for all main entities: `Book`, `Author`, `Category`, `User`, `Loan`, `Password`.
- Basic tests in `Main.java` for each DAO (insert, update, delete, findAll, findById).
- `README.md` updated with project description, roadmap, technologies, and database schema.
- `.gitignore` added for Maven, IDE, and OS temporary files.

### Changed
- Database schema updated: added `Passwords` table.
- Enum `Nationality` added for `Author` model.

### Fixed
- N/A
