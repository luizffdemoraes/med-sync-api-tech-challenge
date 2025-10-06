package br.com.fiap.postech.medsync.auth.infrastructure.gateways;


import br.com.fiap.postech.medsync.auth.domain.entities.Role;
import br.com.fiap.postech.medsync.auth.domain.gateways.RoleGateway;
import br.com.fiap.postech.medsync.auth.infrastructure.persistence.entity.RoleEntity;
import br.com.fiap.postech.medsync.auth.infrastructure.persistence.repository.RoleRepository;

import java.util.Optional;

public class RoleGatewayImpl implements RoleGateway {

    private final RoleRepository roleRepository;

    public RoleGatewayImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByAuthority(String authority) {
        return roleRepository.findByAuthority(authority)
                .map(RoleEntity::toDomain);
    }
}
