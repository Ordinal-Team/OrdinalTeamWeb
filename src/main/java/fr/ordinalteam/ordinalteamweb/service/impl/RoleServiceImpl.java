package fr.ordinalteam.ordinalteamweb.service.impl;

import fr.ordinalteam.ordinalteamweb.model.Role;
import fr.ordinalteam.ordinalteamweb.model.RoleName;
import fr.ordinalteam.ordinalteamweb.repository.RoleRepository;
import fr.ordinalteam.ordinalteamweb.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(final RoleName  name) {
        return roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public Set<Role> initializeDefaultRoles() {
        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(new Role(RoleName.USER));
        defaultRoles.add(new Role(RoleName.BOOSTER));
        defaultRoles.add(new Role(RoleName.MODERATOR));
        defaultRoles.add(new Role(RoleName.ADMINISTRATOR));
        return new HashSet<>(roleRepository.saveAll(defaultRoles));
    }
}