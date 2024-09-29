package org.example.studentrevealexam;

import org.example.studentrevealexam.models.Exams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamDaoTest {

    private ExamDao examDao;
    private Connection connectionMock;
    private PreparedStatement statementMock;
    private ResultSet resultSetMock;

    @BeforeEach
    void setUp() {
        examDao = new ExamDao();
        connectionMock = mock(Connection.class);
        statementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);
    }

    @Test
    void retrieveAllExam() throws SQLException {
        // Set up mock behavior
        when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getString(1)).thenReturn("Math");

        // Inject mocks
        Database.setConnection(connectionMock);

        // Call the method
        Exams exams = examDao.retrieveAllExam();

        // Verify interactions and state changes
        verify(connectionMock, times(1)).prepareStatement(anyString());
        verify(statementMock, times(1)).executeQuery();
        verify(resultSetMock, times(1)).next();
        assertEquals(1, exams.getExamList().size());
        assertEquals("Math", exams.getExamList().get(0));
    }

    @Test
    void retrieveExamNum() throws SQLException {
        // Set up mock behavior
        when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(1)).thenReturn(5);

        // Inject mocks
        Database.setConnection(connectionMock);

        // Call the method
        int examCount = examDao.retrieveExamNum();

        // Verify interactions and state changes
        verify(connectionMock, times(1)).prepareStatement(anyString());
        verify(statementMock, times(1)).executeQuery();
        verify(resultSetMock, times(1)).next();
        assertEquals(5, examCount);
    }
}