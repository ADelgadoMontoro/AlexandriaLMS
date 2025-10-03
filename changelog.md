# Changelog

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
