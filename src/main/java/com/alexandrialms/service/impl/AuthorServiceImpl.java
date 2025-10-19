package com.alexandrialms.service.impl;

import java.util.List;
import java.util.Optional;

import com.alexandrialms.dao.impl.AuthorDAO;
import com.alexandrialms.exception.ValidationException;
import com.alexandrialms.model.Author;
import com.alexandrialms.service.interfaces.AuthorServiceInterface;
import com.alexandrialms.util.ValidationHelper;

public class AuthorServiceImpl implements AuthorServiceInterface {
    private final AuthorDAO authorDAO;

    public AuthorServiceImpl(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    @Override
    public Author createAuthor(Author author) {
        ValidationHelper.validateAuthor(author);
        if (authorExists(author.getAuthorID())) {
            throw new ValidationException("authorID", "AUTHOR_ALREADY_EXISTS",
                    "The author already exists");
        }
        authorDAO.insert(author);
        return author;

    }

    @Override
    public Optional<Author> findAuthorById(int authorId) {
        return Optional.ofNullable(authorDAO.findById(authorId));
    }

    @Override
    public List<Author> findAllAuthors() {
        return authorDAO.findAll();
    }

    @Override
    public List<Author> findAuthorsByName(String name) {
        if (!ValidationHelper.isValidString(name, 2)) {
            throw new ValidationException("name", "INVALID_NAME",
                    "The name must be at least 2 characters long");
        }
        return authorDAO.findByLastName(name);
    }

    @Override
    public List<Author> findAuthorsByNationality(String nationality) {
        if (!ValidationHelper.isValidString(nationality, 2)) {
            throw new ValidationException("name", "INVALID_NAME",
                    "The nationality must be at least 2 characters long");
        }
        return authorDAO.findByNationality(nationality);
    }

    @Override
    public Optional<Author> updateAuthor(Author author) {
        if (!authorExists(author.getAuthorID())) {
            return Optional.empty();
        }
        ValidationHelper.validateAuthor(author);
        boolean updated = authorDAO.update(author);
        if (!updated) {
            return Optional.empty();
        }
        return Optional.of(author);
    }

    @Override
    public boolean deleteAuthor(int authorId) {
        if (!ValidationHelper.isValidAuthorID(authorId, authorDAO)) {
            return false;
        }
        return authorDAO.delete(authorId);
    }

    @Override
    public int deleteAuthorsWithNoBooks() {
        return authorDAO.deleteAuthorsWithNoBooks();
    }

    @Override
    public boolean authorExists(int authorId) {
        return authorDAO.findById(authorId) != null;
    }

    @Override
    public int countAuthors() {
        return authorDAO.findAll().size();
    }

    @Override
    public List<Author> findAuthorsWithBooks() {
        return authorDAO.findAuthorsWithBooks();
    }
    public AuthorServiceImpl() {
        this.authorDAO = new AuthorDAO();}

}
