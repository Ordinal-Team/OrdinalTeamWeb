package fr.ordinalteam.ordinalteamweb.repository;

import fr.ordinalteam.ordinalteamweb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
