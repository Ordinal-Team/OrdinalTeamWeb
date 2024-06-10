package fr.ordinalteam.ordinalteamweb.service.impl;

import fr.ordinalteam.ordinalteamweb.model.Category;
import fr.ordinalteam.ordinalteamweb.repository.CategoryRepository;
import fr.ordinalteam.ordinalteamweb.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(final Long id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public Category saveCategory(final Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public void initializeDefaultCategories() {
        if (this.categoryRepository.count() == 0) {
            final Category devBlogCategory = new Category();
            devBlogCategory.setName("DevBlogs");
            devBlogCategory.setDescription("Technical blogs and development insights from our team.");

            final Category updatesCategory = new Category();
            updatesCategory.setName("API Updates");
            updatesCategory.setDescription("Information and updates about our API changes and improvements.");

            final Category announceCategory = new Category();
            announceCategory.setName("Announcements");
            announceCategory.setDescription("General announcements and important news for our users.");

            this.categoryRepository.save(devBlogCategory);
            this.categoryRepository.save(updatesCategory);
            this.categoryRepository.save(announceCategory);
        }
    }
}
