package fr.ordinalteam.ordinalteamweb.service;

import fr.ordinalteam.ordinalteamweb.model.Announcement;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    List<Announcement> getAllAnnouncements();
    Optional<Announcement> getAnnouncementById(final Long id);
    Announcement saveAnnouncement(final Announcement announcement);
    void deleteAnnouncement(final Long id);
    List<Announcement> findTop3ByOrderByCreatedAtDesc();
    List<Announcement> findAll();


}
