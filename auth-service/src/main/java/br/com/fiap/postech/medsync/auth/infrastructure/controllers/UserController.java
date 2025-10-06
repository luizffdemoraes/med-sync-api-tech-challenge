package br.com.fiap.postech.medsync.auth.infrastructure.controllers;


import br.com.fiap.postech.medsync.auth.application.dtos.requests.PasswordRequest;
import br.com.fiap.postech.medsync.auth.application.dtos.requests.UserRequest;
import br.com.fiap.postech.medsync.auth.application.dtos.responses.UserResponse;
import br.com.fiap.postech.medsync.auth.application.usecases.CreateUserUseCase;
import br.com.fiap.postech.medsync.auth.application.usecases.FindUserByIdUseCase;
import br.com.fiap.postech.medsync.auth.application.usecases.UpdatePasswordUseCase;
import br.com.fiap.postech.medsync.auth.application.usecases.UpdateUserUseCase;
import br.com.fiap.postech.medsync.auth.domain.entities.User;
import br.com.fiap.postech.medsync.auth.infrastructure.config.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UpdatePasswordUseCase updatePasswordUseCase;

    public UserController(CreateUserUseCase createUserUseCase,
                          FindUserByIdUseCase findUserByIdUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          UpdatePasswordUseCase updatePasswordUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.updatePasswordUseCase = updatePasswordUseCase;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequest request) {
        User user = UserMapper.toDomain(request);
        User userSave = this.createUserUseCase.execute(user);
        UserResponse response = UserMapper.toResponse(userSave);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable Integer id) {
        User user = this.findUserByIdUseCase.execute(id);
        UserResponse response = UserMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Integer id, @Valid @RequestBody UserRequest request) {
        User user = UserMapper.toDomain(request);
        User responseUpdate = updateUserUseCase.execute(id, user);
        UserResponse response = UserMapper.toResponse(responseUpdate);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Integer id, @Valid @RequestBody PasswordRequest request) {
        this.updatePasswordUseCase.execute(id, request.password());
        return ResponseEntity.noContent().build();
    }
}
