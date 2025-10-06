package br.com.fiap.postech.medsync.auth.application.usecases;

import br.com.fiap.postech.medsync.auth.domain.entities.Address;
import br.com.fiap.postech.medsync.auth.domain.entities.User;
import br.com.fiap.postech.medsync.auth.domain.gateways.UserGateway;
import br.com.fiap.postech.medsync.auth.infrastructure.config.mapper.UserMapper;
import br.com.fiap.postech.medsync.auth.infrastructure.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiap.postech.medsync.auth.factories.TestDataFactory.createUserRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindUserByIdUseCaseImpTest {

    @InjectMocks
    private FindUserByIdUseCaseImp findUserByIdUseCaseImp;

    @Mock
    private UserGateway userGateway;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = UserMapper.toDomain(createUserRequest());
        user.setId(1);
    }

    @Test
    void execute_shouldReturnUser_whenUserFoundAndAuthorized() {

        when(userGateway.findUserById(1)).thenReturn(user);
        doNothing().when(userGateway).validateSelf(user.getId());

        User response = findUserByIdUseCaseImp.execute(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Rua Exemplo", response.getAddress().getStreet());
        verify(userGateway).findUserById(1);
        verify(userGateway).validateSelf(user.getId());
    }

    @Test
    void execute_shouldThrowBusinessException_whenNotAuthorized() {
        when(userGateway.findUserById(1)).thenReturn(user);
        doThrow(new BusinessException("Não autorizado")).when(userGateway).validateSelf(user.getId());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                findUserByIdUseCaseImp.execute(1)
        );
        assertEquals("Não autorizado", exception.getMessage());
        verify(userGateway).findUserById(1);
        verify(userGateway).validateSelf(user.getId());
    }
}