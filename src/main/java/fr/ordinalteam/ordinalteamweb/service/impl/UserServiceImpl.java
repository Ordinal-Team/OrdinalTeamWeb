package fr.ordinalteam.ordinalteamweb.service.impl;

import fr.ordinalteam.ordinalteamweb.model.Role;
import fr.ordinalteam.ordinalteamweb.model.RoleName;
import fr.ordinalteam.ordinalteamweb.model.User;
import fr.ordinalteam.ordinalteamweb.repository.RoleRepository;
import fr.ordinalteam.ordinalteamweb.repository.UserRepository;
import fr.ordinalteam.ordinalteamweb.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository userRepository, final RoleRepository roleRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User saveUser(final User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        final Set<Role> roles = new HashSet<>();
        roles.add(this.roleRepository.findByName(RoleName.USER).orElseThrow(() -> new RuntimeException("Default role USER not found")));
        user.setRoles(roles);
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public void deleteUser(final Long userId) {
        this.userRepository.deleteById(userId);
    }

    @Override
    public User updateUserRoles(final Long userId, final Set<Role> roles) {
        final User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRoles(roles);
        return this.userRepository.save(user);
    }
}
