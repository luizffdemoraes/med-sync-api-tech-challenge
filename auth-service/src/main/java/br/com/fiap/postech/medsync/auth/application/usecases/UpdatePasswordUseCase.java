package br.com.fiap.postech.medsync.auth.application.usecases;

public interface UpdatePasswordUseCase {
    void execute(Integer id, String password);
}
