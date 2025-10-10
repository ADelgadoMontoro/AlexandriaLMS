package com.alexandrialms.service.interfaces;

import com.alexandrialms.model.Author;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link Author} entities.
 * <p>
 * This interface defines the operations for creating, retrieving, updating, 
 * and deleting authors, as well as performing specific queries such as 
 * finding authors by name or nationality, checking existence, 
 * and counting authors.
 * </p>
 * 
 * <p>
 * The service layer encapsulates business logic and validation rules,
 * providing a clean API for the presentation layer while delegating
 * data access operations to the DAO layer.
 * </p>
 */
public interface AuthorServiceInterface {

    /**
     * Creates and persists a new author in the system.
     * Validates author data before creation and checks for duplicates.
     *
     * @param author the {@link Author} entity to be created
     * @return the created {@link Author} with its generated ID
     * @throws ValidationException if author data is invalid or author already exists
     */
    Author createAuthor(Author author);

    /**
     * Finds an author by its unique identifier.
     *
     * @param authorId the ID of the author to retrieve
     * @return an {@link Optional} containing the found {@link Author}, or empty if not found
     */
    Optional<Author> findAuthorById(int authorId);

    /**
     * Retrieves all authors in the system.
     *
     * @return a list of all {@link Author} entities, ordered by last name and first name
     */
    List<Author> findAllAuthors();

    /**
     * Finds authors whose names match or partially match the specified value.
     * Searches in both first name and last name fields.
     *
     * @param name the name or partial name to search for
     * @return a list of matching {@link Author} entities, ordered by last name
     */
    List<Author> findAuthorsByName(String name);

    /**
     * Finds authors by their nationality.
     * Performs case-insensitive search for exact matches.
     *
     * @param nationality the nationality to search for
     * @return a list of {@link Author} entities matching the nationality, ordered by last name
     */
    List<Author> findAuthorsByNationality(String nationality);

    /**
     * Updates an existing author's information.
     * Validates author data and checks for duplicate names before updating.
     *
     * @param author the {@link Author} entity with updated information
     * @return an {@link Optional} containing the updated {@link Author}, or empty if not found
     * @throws ValidationException if author data is invalid or duplicate name exists
     */
    Optional<Author> updateAuthor(Author author);

    /**
     * Deletes an author by its ID.
     * Only deletes authors that have no associated books in the system.
     *
     * @param authorId the ID of the author to delete
     * @return {@code true} if the author was successfully deleted, {@code false} otherwise
     * @throws ValidationException if the author does not exist or has associated books
     */
    boolean deleteAuthor(int authorId);

    /**
     * Checks whether an author exists by their ID.
     *
     * @param authorId the ID to check
     * @return {@code true} if the author exists, {@code false} otherwise
     */
    boolean authorExists(int authorId);

    /**
     * Counts the total number of authors in the system.
     *
     * @return the total number of authors
     */
    int countAuthors();

    /**
     * Finds authors that have at least one book in the system.
     * Useful for displaying only authors with published works.
     *
     * @return a list of {@link Author} entities that have associated books, ordered by last name
     */
    List<Author> findAuthorsWithBooks();

    /**
     * Deletes authors that have no associated books in the system.
     * This is a maintenance operation to clean up orphaned author records.
     *
     * @return the number of authors that were deleted
     */
    int deleteAuthorsWithNoBooks();
}