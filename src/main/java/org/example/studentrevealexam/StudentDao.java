package org.example.studentrevealexam;

import org.example.studentrevealexam.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StudentDao {
    private static final Logger logger = Logger.getLogger(StudentDao.class.getName());

    public Student retrieveResult(String lg_mat_etud) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        Student student = new Student();

        try {
            connection = Database.getDBConnexion();
            connection.setAutoCommit(false);
            String query = """
                    select *
                    from Etudiant
                    where Etudiant.lg_mat_etud = ?""";
            String query1 = """
                    select etu.lg_mat_etud,
                           etu.str_nom_etud,
                           etu.str_prenoms_etud,
                           etu.dt_date_naissance_etud,
                           etu.str_ecole,
                           GROUP_CONCAT(CONCAT
                                        (str_nom_exam, ':',
                                         int_note_exam) SEPARATOR ',')\
                    from Etudiant etu \
                    inner join EtudiantExam EE on etu.lg_mat_etud = EE.Etudiant \
                    inner join Exam E on EE.Exam = E.lg_exam_id \
                    where etu.lg_mat_etud = ?""";
            statement = connection.prepareStatement(query);
            statement.setString(1, lg_mat_etud);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                statement = connection.prepareStatement(query1);
                statement.setString(1, lg_mat_etud);
                ResultSet resultSet1 = statement.executeQuery();

                if (resultSet1.next()) {
                    student.setLg_mat_etud(resultSet1.getInt(1));
                    student.setStr_nom_etud(resultSet1.getString(2));
                    student.setStr_prenoms_etud(resultSet1.getString(3));
                    student.setDt_date_naissance_etud(resultSet1.getString(4));
                    student.setStr_ecole_etud(resultSet1.getString(5));
                    student.setNotes(resultSet1.getString(6));
                }
            } else {
                return null;
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

        return student;
    }
}
