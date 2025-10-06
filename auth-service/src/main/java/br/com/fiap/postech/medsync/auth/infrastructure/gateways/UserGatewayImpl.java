package br.com.fiap.postech.medsync.auth.infrastructure.gateways;


import br.com.fiap.postech.medsync.auth.domain.entities.User;
import br.com.fiap.postech.medsync.auth.domain.gateways.UserGateway;
import br.com.fiap.postech.medsync.auth.infrastructure.config.mapper.UserMapper;
import br.com.fiap.postech.medsync.auth.infrastructure.exceptions.BusinessException;
import br.com.fiap.postech.medsync.auth.infrastructure.persistence.entity.RoleEntity;
import br.com.fiap.postech.medsync.auth.infrastructure.persistence.entity.UserEntity;
import br.com.fiap.postech.medsync.auth.infrastructure.persistence.repository.RoleRepository;
import br.com.fiap.postech.medsync.auth.infrastructure.persistence.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Date;

public class UserGatewayImpl implements UserGateway {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserGatewayImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User saveUser(User user) {
        UserEntity userEntity = UserMapper.fromDomain(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));

        // Limpe os roles do mapeamento
        userEntity.getRoleEntities().clear();
        // Adicione apenas roleEntities gerenciados pelo JPA
        user.getRoles().forEach(role -> {
            RoleEntity managedRole = roleRepository.getReferenceById(role.getId());
            userEntity.addRole(managedRole);
        });

        UserEntity saved = this.userRepository.save(userEntity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }


    public User findUserById(Integer id) {
        return findUserOrThrow(id);
    }

    @Override
    public User updateUser(Integer id, User userRequest) {
        User user = findUserOrThrow(id);
        validateSelf(id);

        user.setPassword(
                (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty())
                        ? passwordEncoder.encode(userRequest.getPassword())
                        : user.getPassword()
        );
        user.setName(userRequest.getName() != null ? userRequest.getName() : user.getName());
        user.setEmail(userRequest.getEmail() != null ? userRequest.getEmail() : user.getEmail());
        user.setLogin(userRequest.getLogin() != null ? userRequest.getLogin() : user.getLogin());
        user.setAddress(userRequest.getAddress() != null ? userRequest.getAddress() : user.getAddress());
        user.setLastUpdateDate(new Date());

        UserEntity saved = this.userRepository.save(UserMapper.fromDomain(user));
        return UserMapper.toDomain(saved);
    }

    public void updateUserPassword(Integer id, String newPassword) {
        User user = findUserOrThrow(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        this.userRepository.save(UserMapper.fromDomain(user));
    }

    public User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            UserEntity userEntity = this.userRepository.findByEmail(username).get();
            return UserMapper.toDomain(userEntity);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

    public User findUserOrThrow(Integer id) {
        UserEntity userEntity = this.userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Id not found: " + id));
        return UserMapper.toDomain(userEntity);
    }

    public void validateSelf(Integer userId) {
        User me = authenticated();
        if (!me.getId().equals(userId)) {
            throw new BusinessException("Access denied - You can only access your own data");
        }
    }
}
