    package com.alexandrialms.util;

    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;
    import java.util.Properties;


    public class DBConnection {

        private static Connection connection;
        private static Properties properties;
        private DBConnection() { }
        
        static{
            properties = new Properties();
            try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (is == null) {
                throw new RuntimeException("db.properties not found!");
                }
                properties.load(is);
            } catch (Exception e) {
            }
        }

        public static Connection getConnection() throws SQLException {
            if (connection == null || connection.isClosed()) {
                String url = properties.getProperty("db.url");
                String user = properties.getProperty("db.user");
                String password = properties.getProperty("db.password");
                try {
                    Class.forName("org.mariadb.jdbc.Driver");

                    connection = DriverManager.getConnection(url,user,password);
                    System.out.println("Conexión a la base de datos establecida.");
                } catch (ClassNotFoundException e) {
                    throw new SQLException("Driver de MariaDB no encontrado.", e);
                }
            }
            return connection;
        }

        public static void closeConnection() {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Conexión a la base de datos cerrada. ");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
