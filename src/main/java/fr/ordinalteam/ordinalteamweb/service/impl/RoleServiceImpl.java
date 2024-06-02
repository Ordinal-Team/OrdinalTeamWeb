package fr.ordinalteam.ordinalteamweb.service.impl;

import fr.ordinalteam.ordinalteamweb.model.Role;
import fr.ordinalteam.ordinalteamweb.model.RoleName;
import fr.ordinalteam.ordinalteamweb.repository.RoleRepository;
import fr.ordinalteam.ordinalteamweb.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(final Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public List<Role> findAllRoles() {
        return this.roleRepository.findAll();
    }

    @Override
    public Role findByName(final RoleName name) {
        return this.roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public void deleteRole(final Long roleId) {
        this.roleRepository.deleteById(roleId);
    }

    @Override
    public Set<Role> initializeDefaultRoles() {
        final Set<Role> defaultRoles = new HashSet<>();

        if (!this.roleRepository.existsByName(RoleName.USER)) {
            defaultRoles.add(new Role(RoleName.USER));
        }
        if (!this.roleRepository.existsByName(RoleName.BOOSTER)) {
            defaultRoles.add(new Role(RoleName.BOOSTER));
        }
        if (!this.roleRepository.existsByName(RoleName.MODERATOR)) {
            defaultRoles.add(new Role(RoleName.MODERATOR));
        }
        if (!this.roleRepository.existsByName(RoleName.ADMINISTRATOR)) {
            defaultRoles.add(new Role(RoleName.ADMINISTRATOR));
        }

        return new HashSet<>(this.roleRepository.saveAll(defaultRoles));
    }
}