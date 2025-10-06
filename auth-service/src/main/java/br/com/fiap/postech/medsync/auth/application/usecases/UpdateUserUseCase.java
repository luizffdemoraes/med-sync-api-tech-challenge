package br.com.fiap.postech.medsync.auth.application.usecases;


import br.com.fiap.postech.medsync.auth.domain.entities.User;

public interface UpdateUserUseCase {
    User execute(Integer id, User request);
}
