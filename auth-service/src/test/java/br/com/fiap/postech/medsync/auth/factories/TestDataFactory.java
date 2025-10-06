package br.com.fiap.postech.medsync.auth.factories;

import br.com.fiap.postech.medsync.auth.application.dtos.requests.AddressRequest;
import br.com.fiap.postech.medsync.auth.application.dtos.requests.UserRequest;
import br.com.fiap.postech.medsync.auth.application.dtos.responses.AddressResponse;
import br.com.fiap.postech.medsync.auth.application.dtos.responses.UserResponse;
import br.com.fiap.postech.medsync.auth.domain.entities.Address;
import br.com.fiap.postech.medsync.auth.domain.entities.Role;
import br.com.fiap.postech.medsync.auth.domain.entities.User;

import java.util.Date;

public class TestDataFactory {


    public static UserRequest createUserRequest() {
        return new UserRequest(
                "John Doe",
                "johndoe@example.com",
                "johndoe",
                "senha123",
                "ROLE_PATIENT",
                createAddressRequest()
        );
    }

    public static AddressRequest createAddressRequest() {
        return new AddressRequest(
                "Rua Exemplo",
                587L,
                "São Paulo",
                "SP",
                "12345-678"
        );
    }

    public static UserResponse createUserResponse() {
        return new UserResponse(
                1,
                "John Doe",
                "johndoe@example.com",
                "johndoe",
                new Date(),
                createAddressResponse()
        );
    }

    public static AddressResponse createAddressResponse() {
        return new AddressResponse(
                "Rua Exemplo",
                587L,
                "São Paulo",
                "SP",
                "12345-678"
        );
    }

    public static Address createAddress() {
        return new Address(
                "Main Street",
                123L,
                "Sample City",
                "Sample State",
                "12345-678"
        );
    }

    public static User createUser() {
        User user = new User(
                "Test User",
                "user@test.com",
                "testLogin",
                "testPassword",
                createAddress()
        );
        user.setId(1);
        user.setLastUpdateDate(new Date());

        Role role = createRoleClient();
        user.addRole(role);

        return user;
    }

    public static Role createRoleClient() {
        return new Role(1, "ROLE_PATIENT");
    }

}
