package fr.ordinalteam.ordinalteamweb.service;

import fr.ordinalteam.ordinalteamweb.model.Role;
import fr.ordinalteam.ordinalteamweb.model.RoleName;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role createRole(final Role role);
    List<Role> findAllRoles();
    Role findByName(final RoleName name);
    void deleteRole(final Long roleId);
    Set<Role> initializeDefaultRoles();
}