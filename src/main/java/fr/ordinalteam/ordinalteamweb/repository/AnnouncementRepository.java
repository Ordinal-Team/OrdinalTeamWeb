package fr.ordinalteam.ordinalteamweb.repository;

import fr.ordinalteam.ordinalteamweb.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findTop3ByOrderByCreatedAtDesc();
}
