package org.example.studentrevealexam;

import org.example.studentrevealexam.models.Exams;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExamDao {
    private static final Logger logger = Logger.getLogger(ExamDao.class.getName());

    public Exams retrieveAllExam() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        Exams exams = new Exams();

        try {
            connection = Database.getDBConnexion();
            connection.setAutoCommit(false);
            String query = """
                    SELECT str_nom_exam
                    FROM Exam
                    """;
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                exams.setExamList(resultSet.getString(1));
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
            if (null != connection) {
                connection.close();
            }
        }

        return exams;
    }

    public int retrieveExamNum() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int examCount = 0;

        try {
            connection = Database.getDBConnexion();
            connection.setAutoCommit(false);
            String query = """
                    SELECT COUNT(*)
                    FROM Exam
                    """;
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                examCount = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
            if (null != connection) {
                connection.close();
            }
        }

        return examCount;
    }

}
