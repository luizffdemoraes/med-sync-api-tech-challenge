package br.com.fiap.postech.medsync.auth.application.usecases;

import br.com.fiap.postech.medsync.auth.application.dtos.requests.UserRequest;
import br.com.fiap.postech.medsync.auth.domain.entities.Role;
import br.com.fiap.postech.medsync.auth.domain.entities.User;
import br.com.fiap.postech.medsync.auth.domain.gateways.RoleGateway;
import br.com.fiap.postech.medsync.auth.domain.gateways.UserGateway;
import br.com.fiap.postech.medsync.auth.factories.TestDataFactory;
import br.com.fiap.postech.medsync.auth.infrastructure.config.mapper.UserMapper;
import br.com.fiap.postech.medsync.auth.infrastructure.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateUserUseCaseImpTest {

    @InjectMocks
    private CreateUserUseCaseImp createUserUseCaseImp;

    @Mock
    private UserGateway userGateway;

    @Mock
    private RoleGateway roleGateway;

    private User userRequestAdmin;
    private User userRequestClient;
    private Role roleAdmin;
    private Role roleClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // cria duas instâncias separadas a partir do factory (não usa construtor de cópia)
        userRequestAdmin = UserMapper.toDomain(TestDataFactory.createUserRequest());
        userRequestAdmin.setName("Admin");
        userRequestAdmin.setEmail("admin@restaurantsync.com");
        userRequestAdmin.setLogin("adminUser");
        userRequestAdmin.setPassword("adminPass");
        userRequestAdmin.getRoles().clear();
        userRequestAdmin.addRole(new Role(null, "ROLE_DOCTOR"));

        userRequestClient = UserMapper.toDomain(TestDataFactory.createUserRequest());
        userRequestClient.setName("Client");
        userRequestClient.setEmail("client@example.com");
        userRequestClient.setLogin("clientUser");
        userRequestClient.setPassword("clientPass");
        userRequestClient.getRoles().clear();
        userRequestClient.addRole(new Role(null, "ROLE_PATIENT"));

        roleAdmin = new Role(1, "ROLE_DOCTOR");
        roleClient = new Role(2, "ROLE_NURSE");
    }

    @Test
    void execute_shouldCreateAdminUser_whenEmailEndsWithRestaurantsync() {
        when(userGateway.existsUserByEmail(userRequestAdmin.getEmail())).thenReturn(false);
        when(roleGateway.findByAuthority("ROLE_DOCTOR")).thenReturn(Optional.of(roleAdmin));

        when(userGateway.saveUser(ArgumentMatchers.any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1);
            return user;
        });

        User response = createUserUseCaseImp.execute(userRequestAdmin);

        assertNotNull(response);
        assertEquals(1, response.getId());
        verify(userGateway).existsUserByEmail(userRequestAdmin.getEmail());
        verify(roleGateway).findByAuthority("ROLE_DOCTOR");
        verify(userGateway).saveUser(ArgumentMatchers.any(User.class));
    }

    @Test
    void execute_shouldCreateClientUser_whenEmailDoesNotEndWithRestaurantsync() {
        when(userGateway.existsUserByEmail(userRequestClient.getEmail())).thenReturn(false);
        when(roleGateway.findByAuthority("ROLE_PATIENT")).thenReturn(Optional.of(roleClient));

        when(userGateway.saveUser(ArgumentMatchers.any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2);
            return user;
        });

        User response = createUserUseCaseImp.execute(userRequestClient);

        assertNotNull(response);
        assertEquals(2, response.getId());
        verify(userGateway).existsUserByEmail(userRequestClient.getEmail());
        verify(roleGateway).findByAuthority("ROLE_PATIENT");
        verify(userGateway).saveUser(ArgumentMatchers.any(User.class));
    }

    @Test
    void execute_shouldThrowException_whenEmailAlreadyRegistered() {
        when(userGateway.existsUserByEmail(userRequestAdmin.getEmail())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> createUserUseCaseImp.execute(userRequestAdmin));
        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    void execute_shouldThrowException_whenRoleNotFound() {
        when(userGateway.existsUserByEmail(userRequestAdmin.getEmail())).thenReturn(false);
        when(roleGateway.findByAuthority("ROLE_DOCTOR")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createUserUseCaseImp.execute(userRequestAdmin));
        assertEquals("Invalid role: ROLE_DOCTOR", exception.getMessage());
    }
}