package fr.ordinalteam.ordinalteamweb.service;

import fr.ordinalteam.ordinalteamweb.model.Role;
import fr.ordinalteam.ordinalteamweb.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User saveUser(final User user);
    Optional<User> findByUsername(final String username);
    List<User> findAllUsers();
    void deleteUser(final Long userId);
    User updateUserRoles(final Long userId, final Set<Role> roles);
}
