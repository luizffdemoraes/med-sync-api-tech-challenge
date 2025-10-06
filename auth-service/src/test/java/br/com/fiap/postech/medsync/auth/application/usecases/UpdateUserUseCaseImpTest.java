package br.com.fiap.postech.medsync.auth.application.usecases;

import br.com.fiap.postech.medsync.auth.domain.entities.User;
import br.com.fiap.postech.medsync.auth.domain.gateways.UserGateway;
import br.com.fiap.postech.medsync.auth.factories.TestDataFactory;
import br.com.fiap.postech.medsync.auth.infrastructure.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UpdateUserUseCaseImpTest {

    @InjectMocks
    private UpdateUserUseCaseImp updateUserUseCaseImp;

    @Mock
    private UserGateway userGateway;

    private User userRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRequest = TestDataFactory.createUser();
    }

    @Test
    void execute_shouldUpdateUser_whenAuthorized() {
        Integer userId = 1;

        doNothing().when(userGateway).validateSelf(userId);
        when(userGateway.updateUser(eq(userId), any(User.class))).thenReturn(userRequest);

        User response = updateUserUseCaseImp.execute(userId, userRequest);

        assertNotNull(response);
        assertEquals(userRequest.getName(), response.getName());
        verify(userGateway).validateSelf(userId);
        verify(userGateway).updateUser(eq(userId), any(User.class));
    }

    @Test
    void execute_shouldThrowBusinessException_whenNotAuthorized() {
        Integer userId = 1;
        doThrow(new BusinessException("Não autorizado")).when(userGateway).validateSelf(userId);

        BusinessException exception = assertThrows(BusinessException.class, () ->
                updateUserUseCaseImp.execute(userId, userRequest)
        );
        assertEquals("Não autorizado", exception.getMessage());

        verify(userGateway).validateSelf(userId);
        verify(userGateway, never()).updateUser(anyInt(), any(User.class));
    }
}