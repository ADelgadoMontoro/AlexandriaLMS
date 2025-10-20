package com.alexandrialms.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alexandrialms.dao.interfaces.BookDAOInterface;
import com.alexandrialms.model.Author;
import com.alexandrialms.model.Book;
import com.alexandrialms.util.DBConnection;

public class BookDAO implements BookDAOInterface {

    private final AuthorDAO authorDAO = new AuthorDAO();

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                Statement statement = conn.createStatement();
                ResultSet rs = statement
                        .executeQuery("SELECT book_id, title, isbn, publication_year, category_id FROM books;");) {
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Book findById(Integer bookID) {
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where book_id = ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookID);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Book book) {
        String sql = "INSERT INTO books (title, isbn, publication_year, category_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getIsbn());
            pstm.setInt(3, book.getPubYear());
            pstm.setInt(4, book.getCategoryId());
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Book book) {
        String sql = "UPDATE Book SET title = ?, isbn = ?, publication_year = ?, category_id = ? WHERE book_id = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getIsbn());
            pstm.setInt(3, book.getPubYear());
            pstm.setInt(4, book.getCategoryId());
            pstm.setInt(5, book.getBookID());

            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer bookID) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookID);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Book mapResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookID(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setIsbn(rs.getString("isbn"));
        book.setPubYear(rs.getInt("publication_year"));
        book.setCategoryId(rs.getInt("category_id"));

        return book;
    }

    @Override
    public Book findByISBN(String isbn) {
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where isbn = ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, isbn);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> findByTitle(String partialTitle) {
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where LOWER(title) LIKE LOWER(?);";
        List<Book> books = new ArrayList<>();
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, "%" + partialTitle + "%");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> findByPublicationYear(int year) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where publication_year = ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, year);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public List<Book> findByPublicationYearRange(int startYear, int endYear) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where publication_year BETWEEN ? AND ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, startYear);
            pstm.setInt(2, endYear);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public List<Book> findByCategory(int categoryId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where category_id = ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, categoryId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public List<Book> findByCategoryName(String categoryName) {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT b.book_id, b.title, b.isbn, b.publication_year, b.category_id
                FROM books b
                JOIN categories c ON b.category_id = c.category_id
                WHERE LOWER(c.name) = LOWER(?);
                """;
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, categoryName);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public List<Book> searchBooks(String searchTerm) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where LOWER(title) LIKE LOWER(?) OR LOWER(isbn) LIKE LOWER(?);";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            String likeTerm = "%" + searchTerm + "%";
            pstm.setString(1, likeTerm);
            pstm.setString(2, likeTerm);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public List<Book> findByTitleAndYear(String title, int year) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where LOWER(title) LIKE LOWER(?) AND publication_year = ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, "%" + title + "%");
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public List<Book> findByCategoryAndYear(int categoryId, int year) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where category_id = ? AND publication_year = ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, categoryId);
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public List<Book> findByTitlePaginated(String partialTitle, int limit, int offset) {// Usar LIMIT y OFFSET para
                                                                                        // paginación
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where LOWER(title) LIKE LOWER(?) LIMIT ? OFFSET ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, "%" + partialTitle + "%");
            pstm.setInt(2, limit);
            pstm.setInt(3, offset);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public List<Book> findByCategoryPaginated(int categoryId, int limit, int offset) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where category_id = ? LIMIT ? OFFSET ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, categoryId);
            pstm.setInt(2, limit);
            pstm.setInt(3, offset);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public List<Book> findAllPaginated(int limit, int offset) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books LIMIT ? OFFSET ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, limit);
            pstm.setInt(2, offset);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return books;
    }

    @Override
    public int countBooksByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) AS cuenta FROM books WHERE category_id = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, categoryId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("cuenta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int countBooksByPublicationYear(int year) {
        String sql = "SELECT COUNT(*) AS cuenta FROM books WHERE publication_year = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, year);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("cuenta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Map<Integer, Integer> getBooksCountByYear() {
        Map<Integer, Integer> booksMap = new HashMap<>();
        String sql = """
                SELECT publication_year, COUNT(*) libros
                FROM books
                GROUP BY publication_year
                ORDER BY 1;
                """;
        ;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                booksMap.put(rs.getInt("publication_year"), rs.getInt("libros"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksMap;
    }

    @Override
    public Map<Integer, Integer> getBooksCountByCategory() {
        Map<Integer, Integer> booksMap = new HashMap<>();
        String sql = """
                SELECT category_id, COUNT(*) libros
                FROM books
                GROUP BY category_id
                ORDER BY 1;
                """;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                booksMap.put(rs.getInt("category_id"), rs.getInt("libros"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksMap;
    }

    @Override
    public List<Book> findAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT b.book_id, b.title, b.isbn, b.publication_year, b.category_id
                FROM books b
                WHERE EXISTS (
                SELECT 1 FROM copies c
                LEFT JOIN loans l ON c.copy_id = l.copy_id AND l.return_date IS NULL
                WHERE c.book_id = b.book_id AND l.loan_id IS NULL
                );
                """;// En esta sentencia uso el 1 en el SELECT porque no me interesa seleccionar
                    // ningun campo
                    // concreto, solo ver si existe alguna fila que cumpla la condición.Y el left join
                    // es para que me traiga todas las copias, aunque no tengan prestamos asociados.
        try (
                Connection conn = DBConnection.getConnection();) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> findUnavailableBooks() {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT b.book_id, b.title, b.isbn, b.publication_year, b.category_id
                FROM books b
                WHERE NOT EXISTS (
                SELECT 1 FROM copies c
                LEFT JOIN loans l ON c.copy_id = l.copy_id AND l.return_date IS NULL
                WHERE c.book_id = b.book_id AND l.loan_id IS NULL
                );
                """;// En esta sentencia uso el 1 en el SELECT porque no me interesa seleccionar
                    // ningun campo
                    // concreto, solo ver si existe alguna fila que cumpla la condición. Y el left join
                    // es para que me traiga todas las copias, aunque no tengan prestamos asociados.
        try (
                Connection conn = DBConnection.getConnection();) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public int getAvailableCopiesCount(int bookId) {
        String sql = """
                SELECT COUNT(*) AS available_count
                FROM copies c
                LEFT JOIN loans l ON c.copy_id = l.copy_id AND l.return_date IS NULL
                WHERE c.book_id = ? AND l.loan_id IS NULL;
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("available_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getTotalCopiesCount(int bookId) {
        String sql = "SELECT COUNT(*) AS total_count FROM copies WHERE book_id = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Book> findMostBorrowedBooks(int limit) {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT b.book_id, b.title, b.isbn, b.publication_year, b.category_id, COUNT(l.loan_id) AS borrow_count
                FROM books b
                JOIN copies c ON b.book_id = c.book_id
                JOIN loans l ON c.copy_id = l.copy_id
                GROUP BY b.book_id, b.title, b.isbn, b.publication_year, b.category_id
                ORDER BY borrow_count DESC
                LIMIT ?;
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, limit);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> findRecentlyAddedBooks(int limit) {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT book_id, title, isbn, publication_year, category_id
                FROM books
                ORDER BY book_id DESC
                LIMIT ?;
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, limit);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public boolean existsByISBN(String isbn) {
        String sql = "SELECT 1 FROM books WHERE isbn = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, isbn);
            ResultSet rs = pstm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existsByTitleAndYear(String title, int year) {
        String sql = "SELECT 1 FROM books WHERE LOWER(title) = LOWER(?) AND publication_year = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, title);
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int countAllBooks() {
        String sql = "SELECT COUNT(*) AS total FROM books;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteBooksByCategory(int categoryId) {
        String sql = "DELETE FROM books WHERE category_id = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, categoryId);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateBooksCategory(int oldCategoryId, int newCategoryId) {
        String sql = "UPDATE books SET category_id = ? WHERE category_id = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, newCategoryId);
            pstm.setInt(2, oldCategoryId);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteBooksWithNoCopies() {
        String sql = """
                DELETE FROM books
                WHERE book_id NOT IN (SELECT DISTINCT book_id FROM copies);
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            return pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ||--------------------------- METHODS USING book_author TABLE ----------------------------||

    @Override
    public boolean addAuthorToBook(int bookId, int authorId) {
        String sql = "INSERT INTO book_author (book_id, author_id) VALUES (?, ?);";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookId);
            pstm.setInt(2, authorId);
            int rowsAffected = pstm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeAuthorFromBook(int bookId, int authorId) {
        String sql = "DELETE FROM book_author WHERE book_id = ? AND author_id = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookId);
            pstm.setInt(2, authorId);
            int rowsAffected = pstm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setBookAuthors(int bookId, List<Integer> authorIds) {
        String deleteSql = "DELETE FROM book_author WHERE book_id = ?;";
        String insertSql = "INSERT INTO book_author (book_id, author_id) VALUES (?, ?);";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement deletePstm = conn.prepareStatement(deleteSql)) {
                deletePstm.setInt(1, bookId);
                deletePstm.executeUpdate();
            }

            try (PreparedStatement insertPstm = conn.prepareStatement(insertSql)) {
                for (Integer authorId : authorIds) {
                    insertPstm.setInt(1, bookId);
                    insertPstm.setInt(2, authorId);
                    insertPstm.addBatch();
                }
                insertPstm.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<Author> getBookAuthors(int bookId) {
        List<Author> authors = new ArrayList<>();
        String sql = """
                SELECT a.author_id, a.first_name, a.last_name, a.birth_date, nationality
                FROM authors a
                JOIN book_author ba ON a.author_id = ba.author_id
                WHERE ba.book_id = ?;
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Author author = authorDAO.mapResultSet(rs);
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public List<Book> findByAuthor(int authorId) {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT b.book_id, b.title, b.isbn, b.publication_year, b.category_id
                FROM books b
                JOIN book_author ba ON b.book_id = ba.book_id
                WHERE ba.author_id = ?;
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, authorId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT DISTINCT b.book_id, b.title, b.isbn, b.publication_year, b.category_id
                FROM books b
                JOIN book_author ba ON b.book_id = ba.book_id
                JOIN authors a ON ba.author_id = a.author_id
                WHERE LOWER(a.first_name) LIKE LOWER(?) OR LOWER(a.last_name) LIKE LOWER(?);
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            String likeTerm = "%" + authorName + "%";
            pstm.setString(1, likeTerm);
            pstm.setString(2, likeTerm);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> findByAuthorAndCategory(int authorId, int categoryId) {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT b.book_id, b.title, b.isbn, b.publication_year, b.category_id
                FROM books b
                JOIN book_author ba ON b.book_id = ba.book_id
                WHERE ba.author_id = ? AND b.category_id = ?;
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, authorId);
            pstm.setInt(2, categoryId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public boolean hasAuthor(int bookId, int authorId) {
        String sql = "SELECT 1 FROM book_author WHERE book_id = ? AND author_id = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookId);
            pstm.setInt(2, authorId);
            ResultSet rs = pstm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int countBooksByAuthor(int authorId) {
        String sql = """
                SELECT COUNT(*) AS total
                FROM book_author
                WHERE author_id = ?;
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, authorId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Book> findByMultipleAuthors(List<Integer> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Book> books = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT DISTINCT b.book_id, b.title, b.isbn, b.publication_year, b.category_id
                FROM books b
                JOIN book_author ba ON b.book_id = ba.book_id
                WHERE ba.author_id IN (
                """);

        for (int i = 0; i < authorIds.size(); i++) {
            sql.append("?");
            if (i < authorIds.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(");");

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql.toString());) {

            for (int i = 0; i < authorIds.size(); i++) {
                pstm.setInt(i + 1, authorIds.get(i));
            }

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> searchBooksWithAuthors(String searchTerm) {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT DISTINCT b.book_id, b.title, b.isbn, b.publication_year, b.category_id
                FROM books b
                LEFT JOIN book_author ba ON b.book_id = ba.book_id
                LEFT JOIN authors a ON ba.author_id = a.author_id
                WHERE LOWER(b.title) LIKE LOWER(?)
                OR LOWER(b.isbn) LIKE LOWER(?)
                OR LOWER(a.first_name) LIKE LOWER(?)
                OR LOWER(a.last_name) LIKE LOWER(?);
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            String likeTerm = "%" + searchTerm + "%";
            pstm.setString(1, likeTerm);
            pstm.setString(2, likeTerm);
            pstm.setString(3, likeTerm);
            pstm.setString(4, likeTerm);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

}