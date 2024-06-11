package fr.ordinalteam.ordinalteamweb.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private boolean emailVerified;
    private String verificationToken;
    private boolean twoFactorEnabled;
    private String discordAccountId;

    @Transient
    private String rolesString;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    public void setEmailVerified(final boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getVerificationToken() {
        return this.verificationToken;
    }

    public void setVerificationToken(final String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public boolean isTwoFactorEnabled() {
        return this.twoFactorEnabled;
    }

    public void setTwoFactorEnabled(final boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getDiscordAccountId() {
        return this.discordAccountId;
    }

    public void setDiscordAccountId(final String discordAccountId) {
        this.discordAccountId = discordAccountId;
    }

    public String getRolesString() {
        return this.rolesString;
    }

    public void setRolesString(final String rolesString) {
        this.rolesString = rolesString;
    }
}
