package fr.ordinalteam.ordinalteamweb.config;

import fr.ordinalteam.ordinalteamweb.service.CategoryService;
import fr.ordinalteam.ordinalteamweb.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final CategoryService categoryService;

    public DataInitializer(final RoleService roleService, final CategoryService categoryService) {
        this.roleService = roleService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(final String... args) throws Exception {
        this.roleService.initializeDefaultRoles();
        this.categoryService.initializeDefaultCategories();
    }
}