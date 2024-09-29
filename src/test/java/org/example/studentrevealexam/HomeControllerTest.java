package org.example.studentrevealexam;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.studentrevealexam.models.Exams;
import org.example.studentrevealexam.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    private HomeController homeController;
    private StudentDao studentDaoMock;
    private ExamDao examDaoMock;

    @BeforeEach
    void setUp() {
        // Initialize JavaFX environment
        new JFXPanel();

        homeController = new HomeController();
        studentDaoMock = mock(StudentDao.class);
        examDaoMock = mock(ExamDao.class);

        homeController.studentDao = studentDaoMock;
        homeController.examDao = examDaoMock;

        // Initialize JavaFX components
        Platform.runLater(() -> {
            homeController.matEtudTextField = new TextField();
            homeController.submitBox = new VBox();
            homeController.resultBox = new VBox();
            homeController.infoText1 = new Label();
            homeController.successImage = new ImageView();
            homeController.lostImage = new ImageView();
        });
    }

    @Test
    void onHelloButtonClick() throws SQLException {
        // Set up mock behavior
        when(studentDaoMock.retrieveResult(anyString())).thenReturn(new Student());
        when(examDaoMock.retrieveExamNum()).thenReturn(5);

        // Set text field value
        Platform.runLater(() -> homeController.matEtudTextField.setText("0073032"));

        // Call the method
        Platform.runLater(() -> homeController.onHelloButtonClick());

        // Verify interactions and state changes
        verify(studentDaoMock, times(1)).retrieveResult("0073032");
        verify(examDaoMock, times(1)).retrieveExamNum();
    }

    @Test
    void restart() {
        // Call the method
        Platform.runLater(() -> homeController.restart());

        // Verify state changes
        Platform.runLater(() -> {
            assertFalse(homeController.detailsBox.isVisible());
            assertTrue(homeController.resultBox.isVisible());
        });
    }

    @Test
    void onRevealDetails() throws SQLException {
        // Set up mock behavior
        Student student = new Student();
        student.setStr_nom_etud("John");
        student.setStr_prenoms_etud("Doe");
        student.setDt_date_naissance_etud("01/01/2000");
        student.setStr_ecole_etud("Test School");
        student.setNotes(Map.of("Math", 15, "Science", 18).toString());
        when(studentDaoMock.retrieveResult(anyString())).thenReturn(student);
        when(examDaoMock.retrieveAllExam()).thenReturn(new Exams());

        // Set student and noteTotal
        homeController.student = student;
        homeController.noteTotal = 33;
        homeController.examCount = 2;

        // Call the method
        Platform.runLater(() -> homeController.onRevealDetails());

        // Verify state changes
        Platform.runLater(() -> {
            assertTrue(homeController.detailsBox.isVisible());
            assertEquals("Nom et prénoms: John Doe", homeController.fullname.getText());
            assertEquals("Date de naissance: 01/01/2000", homeController.birthday.getText());
            assertEquals("Ecole : Test School", homeController.school.getText());
            assertEquals("Total point : 33/40", homeController.totalPoint.getText());
            assertEquals("Moyenne : 16.5", homeController.moyenne.getText());
        });
    }
}