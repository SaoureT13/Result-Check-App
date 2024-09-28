package org.example.studentrevealexam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_HOSTNAME = "jdbc:mysql://ls-0f19f4268096a452a869b6f8467bc299c51da519.cz6cgwgke8xd.eu-west-3.rds.amazonaws.com:3306/db0073032";
//    private static final String DB_HOSTNAME = "jdbc:mysql://localhost:3306/db0073032";

    private static final String DB_USER = "user0073032";
//    private static final String DB_USER = "root";

    private static final String DB_PASSWORD = "Yf3IgyBsOPa34WR";
//    private static final String DB_PASSWORD = "S@oured_13";


    private Database() {

    }

    public static Connection getDBConnexion() throws SQLException {
        Connection connection = null;

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        try {
            connection = DriverManager.getConnection(DB_HOSTNAME, DB_USER, DB_PASSWORD);
            return connection;
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        return connection;
    }
}
