package fr.ordinalteam.ordinalteamweb.service.impl;

import fr.ordinalteam.ordinalteamweb.dto.Response;
import fr.ordinalteam.ordinalteamweb.model.Role;
import fr.ordinalteam.ordinalteamweb.model.RoleName;
import fr.ordinalteam.ordinalteamweb.model.User;
import fr.ordinalteam.ordinalteamweb.repository.RoleRepository;
import fr.ordinalteam.ordinalteamweb.repository.UserRepository;
import fr.ordinalteam.ordinalteamweb.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {


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
        final User user = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRoles(roles);
        return this.userRepository.save(user);
    }

    @Override
    public Response registerUser(final String username, final String email, final String password, final String cPassword) {
        final Response response = new Response();
        if (!password.equals(cPassword)) {
            response.setSuccess(false);
            response.setErrorMessage("Passwords do not match.");
            return response;
        }

        if (password.length() < 7) {
            response.setSuccess(false);
            response.setErrorMessage("Your password size must be higher than 7");
            return response;
        }

        if (this.userRepository.findByUsername(username).isPresent()) {
            response.setSuccess(false);
            response.setErrorMessage("User already exists with the same name");
            return response;
        }

        if (this.userRepository.findByEmail(email).isPresent()) {
            response.setSuccess(false);
            response.setErrorMessage("User already exists with the same email");
            return response;
        }

        final boolean isFirstUser = this.userRepository.count() == 0;
        final User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(this.passwordEncoder.encode(password));
        final Set<Role> roles = new HashSet<>();
        if (isFirstUser) {
            roles.add(this.roleRepository.findByName(RoleName.ADMINISTRATOR).orElseThrow(() -> new RuntimeException("ADMINISTRATOR role not found")));
        } else {
            roles.add(this.roleRepository.findByName(RoleName.USER).orElseThrow(() -> new RuntimeException("Default role USER not found")));
        }
        user.setRoles(roles);
        this.userRepository.save(user);
        response.setSuccess(true);
        return response;
    }

    @Override
    public User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            return userRepository.findByUsername(authentication.getName()).orElse(null);
        }
        return null;    }

    @Override
    public void verifyEmail(final String token) {
        final Optional<User> userOptional = this.userRepository.findByVerificationToken(token);
        if (userOptional.isPresent()) {
            final User user = userOptional.get();
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            this.userRepository.save(user);
        }
    }

    @Override
    public void enableTwoFactorAuthentication(final String username) {
        final Optional<User> userOptional = this.userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            final User user = userOptional.get();
            user.setTwoFactorEnabled(true);
            this.userRepository.save(user);
        }
    }

    @Override 
    public void linkDiscordAccount(final String username, final String discordAccountId) {
        final Optional<User> userOptional = this.userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            final User user = userOptional.get();
            user.setDiscordAccountId(discordAccountId);
            this.userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
