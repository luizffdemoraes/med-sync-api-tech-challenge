package br.com.fiap.postech.medsync.history.infrastructure.exceptions.handler;

import br.com.fiap.postech.medsync.history.infrastructure.exceptions.InvalidPatientIdException;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.MedicalRecordNotFoundException;
import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.graphql.execution.ErrorType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GraphQLExceptionHandlerTest {

    private GraphQLExceptionHandler handler;
    private DataFetchingEnvironment env;


    @BeforeEach
    void setUp() {
        handler = new GraphQLExceptionHandler();
        env = mock(DataFetchingEnvironment.class);

        // Mock do ExecutionStepInfo
        ExecutionStepInfo executionStepInfo = mock(ExecutionStepInfo.class);
        when(env.getExecutionStepInfo()).thenReturn(executionStepInfo);

        // Mock adequado do ResultPath
        ResultPath mockPath = mock(ResultPath.class);
        when(executionStepInfo.getPath()).thenReturn(mockPath);

        // Mock do field e source location
        Field field = mock(Field.class);
        SourceLocation sourceLocation = new SourceLocation(1, 1);

        when(env.getField()).thenReturn(field);
        when(field.getSourceLocation()).thenReturn(sourceLocation);
    }

    @Test
    void testInvalidPatientIdException() {
        var ex = new InvalidPatientIdException("Paciente inválido");
        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertEquals(ErrorType.BAD_REQUEST, error.getErrorType());
        assertEquals("Paciente inválido", error.getMessage());
        assertNotNull(error.getLocations());
        assertNotNull(error.getPath());
    }

    @Test
    void testMedicalRecordNotFoundException() {
        var ex = new MedicalRecordNotFoundException("Registro não encontrado");
        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertEquals(ErrorType.NOT_FOUND, error.getErrorType());
        assertEquals("Registro não encontrado", error.getMessage());
        assertNotNull(error.getLocations());
        assertNotNull(error.getPath());
    }

    @Test
    void testMedicalRecordNotFoundExceptionWithPatientId() {
        var ex = new MedicalRecordNotFoundException(123L);
        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertEquals(ErrorType.NOT_FOUND, error.getErrorType());
        assertEquals("Medical record not found for patient with ID: 123", error.getMessage());
        assertNotNull(error.getLocations());
        assertNotNull(error.getPath());
    }

    @Test
    void testInvalidPatientIdExceptionWithPatientId() {
        var ex = new InvalidPatientIdException(456L);
        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertEquals(ErrorType.BAD_REQUEST, error.getErrorType());
        assertEquals("Invalid patient ID: 456", error.getMessage());
        assertNotNull(error.getLocations());
        assertNotNull(error.getPath());
    }

    @Test
    void testIllegalArgumentException() {
        var ex = new IllegalArgumentException("Argumento inválido");
        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertEquals(ErrorType.BAD_REQUEST, error.getErrorType());
        assertEquals("Argumento inválido", error.getMessage());
        assertNotNull(error.getLocations());
        assertNotNull(error.getPath());
    }

    @Test
    void testOtherException() {
        var ex = new RuntimeException("Erro genérico");
        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertEquals(ErrorType.INTERNAL_ERROR, error.getErrorType());
        assertEquals("Internal server error", error.getMessage());
        assertNotNull(error.getLocations());
        assertNotNull(error.getPath());
    }
}