package br.com.fiap.postech.medsync.auth.infrastructure.controllers;

import br.com.fiap.postech.medsync.auth.application.dtos.requests.PasswordRequest;
import br.com.fiap.postech.medsync.auth.application.dtos.requests.UserRequest;
import br.com.fiap.postech.medsync.auth.application.dtos.responses.UserResponse;
import br.com.fiap.postech.medsync.auth.application.usecases.CreateUserUseCase;
import br.com.fiap.postech.medsync.auth.application.usecases.FindUserByIdUseCase;
import br.com.fiap.postech.medsync.auth.application.usecases.UpdatePasswordUseCase;
import br.com.fiap.postech.medsync.auth.application.usecases.UpdateUserUseCase;
import br.com.fiap.postech.medsync.auth.domain.entities.User;
import br.com.fiap.postech.medsync.auth.factories.TestDataFactory;
import br.com.fiap.postech.medsync.auth.infrastructure.config.mapper.UserMapper;
import br.com.fiap.postech.medsync.auth.infrastructure.persistence.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private FindUserByIdUseCase findUserByIdUseCase;

    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @MockBean
    private UpdatePasswordUseCase updatePasswordUseCase;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUserSuccess() throws Exception {
        // usa TestDataFactory do projeto para criar request (mesmo padrão do exemplo)
        UserRequest createRequest = TestDataFactory.createUserRequest();
        User userDomain = UserMapper.toDomain(createRequest);
        UserResponse userResponse = UserMapper.toResponse(userDomain);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(createUserUseCase.execute(any(User.class))).thenReturn(userDomain);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userResponse.id()))
                .andExpect(jsonPath("$.name").value(userResponse.name()))
                .andExpect(jsonPath("$.email").value(userResponse.email()))
                .andExpect(jsonPath("$.login").value(userResponse.login()));
    }

    @Test
    void testCreateUserBadRequest() throws Exception {
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        Integer userId = 1;
        User userDomain = UserMapper.toDomain(TestDataFactory.createUserRequest());

        when(findUserByIdUseCase.execute(userId)).thenReturn(userDomain);

        mockMvc.perform(get("/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDomain.getId()))
                .andExpect(jsonPath("$.name").value(userDomain.getName()));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        Integer userId = 1;
        UserRequest updateRequest = TestDataFactory.createUserRequest();
        User userDomain = UserMapper.toDomain(updateRequest);

        when(updateUserUseCase.execute(eq(userId), any(User.class))).thenReturn(userDomain);

        mockMvc.perform(put("/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDomain.getId()))
                .andExpect(jsonPath("$.name").value(userDomain.getName()));
    }

    @Test
    void testUpdatePasswordSuccess() throws Exception {
        Integer userId = 1;
        PasswordRequest passwordRequest = new PasswordRequest("novaSenha123");

        doNothing().when(updatePasswordUseCase).execute(userId, passwordRequest.password());

        mockMvc.perform(patch("/v1/users/{id}/password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isNoContent());
    }
}