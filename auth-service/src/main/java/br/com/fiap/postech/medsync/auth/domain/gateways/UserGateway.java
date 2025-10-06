package br.com.fiap.postech.medsync.auth.domain.gateways;


import br.com.fiap.postech.medsync.auth.domain.entities.User;

public interface UserGateway {
    User saveUser(User user);
    boolean existsUserByEmail(String email);
    User findUserById(Integer id);
    User updateUser(Integer id, User user);
    void updateUserPassword(Integer id, String newPassword);
    User authenticated();
    User findUserOrThrow(Integer id);
    void validateSelf(Integer userId);
}