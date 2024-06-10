package fr.ordinalteam.ordinalteamweb.service;

import fr.ordinalteam.ordinalteamweb.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(final Long id);
    Category saveCategory(final Category category);
    void initializeDefaultCategories();
}
