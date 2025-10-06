package br.com.fiap.postech.medsync.auth.application.usecases;


import br.com.fiap.postech.medsync.auth.domain.entities.Role;
import br.com.fiap.postech.medsync.auth.domain.entities.User;
import br.com.fiap.postech.medsync.auth.domain.gateways.RoleGateway;
import br.com.fiap.postech.medsync.auth.domain.gateways.UserGateway;
import br.com.fiap.postech.medsync.auth.infrastructure.exceptions.BusinessException;

public class CreateUserUseCaseImp implements CreateUserUseCase {

    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    public CreateUserUseCaseImp(UserGateway userGateway,
                                RoleGateway roleGateway) {
        this.userGateway = userGateway;
        this.roleGateway = roleGateway;
    }

    public User execute(User user) {
        if (userGateway.existsUserByEmail(user.getEmail())) {
            throw new BusinessException("Email already registered");
        }
        Role role = roleGateway.findByAuthority(user.getRoles().iterator().next().getAuthority())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + user.getRoles()));

        return userGateway.saveUser(user);
    }
}
