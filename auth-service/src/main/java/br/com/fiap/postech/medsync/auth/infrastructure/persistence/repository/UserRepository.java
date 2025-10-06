package br.com.fiap.postech.medsync.auth.infrastructure.persistence.repository;


import br.com.fiap.postech.medsync.auth.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);

	boolean existsByEmail(String email);
}
