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
        return authorDAO.findByLastName(name);
    }

    @Override
    public List<Author> findAuthorsByNationality(String nationality) {
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
        if (!authorExists(authorId)) {
            throw new ValidationException("authorId", "NON_EXISTING_AUTHOR", "The author does not exist");
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

}
