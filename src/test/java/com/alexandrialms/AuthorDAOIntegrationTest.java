package com.alexandrialms;

import com.alexandrialms.dao.impl.AuthorDAO;
import com.alexandrialms.model.Author;
import com.alexandrialms.util.DBConnection;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class AuthorDAOIntegrationTest {

    private AuthorDAO authorDAO;

    @Before
    public void setUp() throws Exception {
        authorDAO = new AuthorDAO();

        // Crear tabla authors en la base de datos H2 en memoria
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            // Limpiar si existía por alguna prueba previa
            stmt.execute("DROP TABLE IF EXISTS authors");

            stmt.execute("CREATE TABLE authors (" +
                    "author_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "first_name VARCHAR(100), " +
                    "last_name VARCHAR(100), " +
                    "nationality VARCHAR(100), " +
                    "birth_date DATE" +
                    ")");

            // Insertar un autor de prueba
            stmt.execute("INSERT INTO authors (first_name, last_name, nationality, birth_date) VALUES ('Juan', 'Pérez', 'Española', '1980-05-12')");
            stmt.execute("INSERT INTO authors (first_name, last_name, nationality, birth_date) VALUES ('Ana', 'García', 'Española', '1990-03-22')");
        }
    }

    @AfterClass
    public static void tearDownClass() {
        // Cerrar la conexión; H2 en memoria desaparecerá al cerrar la VM o cuando se cierre la conexión con DB_CLOSE_DELAY=0
        DBConnection.closeConnection();
    }

    @Test
    public void testFindAllAndFindById() {
        List<Author> all = authorDAO.findAll();
        assertNotNull(all);
        assertTrue(all.size() >= 2);

        Author first = all.get(0);
        Author byId = authorDAO.findById(first.getAuthorID());
        assertNotNull(byId);
        assertEquals(first.getFirstName(), byId.getFirstName());
    }

    @Test
    public void testInsertAndExists() {
        Author a = new Author();
        a.setFirstName("Miguel");
        a.setLastName("Cervantes");
        a.setNationality("Española");
        a.setBirthDate(LocalDate.of(1547, 9, 29));

        boolean inserted = authorDAO.insert(a);
        assertTrue(inserted);
        assertNotNull(a.getAuthorID());

        assertTrue(authorDAO.existsByFullName("Miguel", "Cervantes"));

        // Cleanup
        boolean deleted = authorDAO.delete(a.getAuthorID());
        assertTrue(deleted);
    }
}
