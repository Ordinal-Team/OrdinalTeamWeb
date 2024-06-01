package fr.ordinalteam.ordinalteamweb.repository;

import fr.ordinalteam.ordinalteamweb.model.Role;
import fr.ordinalteam.ordinalteamweb.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(final RoleName name);

}
