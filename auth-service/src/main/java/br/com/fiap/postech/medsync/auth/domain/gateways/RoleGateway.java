package br.com.fiap.postech.medsync.auth.domain.gateways;



import br.com.fiap.postech.medsync.auth.domain.entities.Role;

import java.util.Optional;

public interface RoleGateway {
    Optional<Role> findByAuthority(String authority);
}
