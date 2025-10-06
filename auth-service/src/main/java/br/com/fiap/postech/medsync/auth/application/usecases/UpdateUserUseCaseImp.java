package br.com.fiap.postech.medsync.auth.application.usecases;


import br.com.fiap.postech.medsync.auth.domain.entities.User;
import br.com.fiap.postech.medsync.auth.domain.gateways.UserGateway;

public class UpdateUserUseCaseImp implements UpdateUserUseCase{

    private final UserGateway userGateway;

    public UpdateUserUseCaseImp(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public User execute(Integer id, User user) {
        this.userGateway.validateSelf(id);
        return this.userGateway.updateUser(id, user);
    }
}
