package fr.ordinalteam.ordinalteamweb.config;

import fr.ordinalteam.ordinalteamweb.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;

    public DataInitializer(final RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(final String... args) throws Exception {
        roleService.initializeDefaultRoles();
    }
}