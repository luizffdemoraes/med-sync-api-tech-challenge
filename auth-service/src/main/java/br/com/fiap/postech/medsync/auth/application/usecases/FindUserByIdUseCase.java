package br.com.fiap.postech.medsync.auth.application.usecases;


import br.com.fiap.postech.medsync.auth.domain.entities.User;

public interface FindUserByIdUseCase {
    User execute(Integer id);
}
