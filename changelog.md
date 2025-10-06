# Changelog — AlexandriaLMS

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
