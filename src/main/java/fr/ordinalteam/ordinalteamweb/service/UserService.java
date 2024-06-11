package fr.ordinalteam.ordinalteamweb.service;

import fr.ordinalteam.ordinalteamweb.dto.Response;
import fr.ordinalteam.ordinalteamweb.model.Role;
import fr.ordinalteam.ordinalteamweb.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User saveUser(User user);
    Optional<User> findByUsername(String username);
    List<User> findAllUsers();
    void deleteUser(Long userId);
    User updateUserRoles(Long userId, Set<Role> roles);
    Response registerUser(String username, String email, String password, String cPassword);
    User getCurrentUser();
    void verifyEmail(String token);
    void enableTwoFactorAuthentication(String username);
    void linkDiscordAccount(String username, String discordAccountId);
    List<User> findAll();
    User findById(Long id);

    // New methods for updating user details
    void updateEmailVerified(Long userId, boolean status);
    void updateTwoFactorEnabled(Long userId, boolean status);
    void updateUserDetails(User user);
}
