package fr.ordinalteam.ordinalteamweb.service.impl;

import fr.ordinalteam.ordinalteamweb.model.Announcement;
import fr.ordinalteam.ordinalteamweb.repository.AnnouncementRepository;
import fr.ordinalteam.ordinalteamweb.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementServiceImpl(final AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        return this.announcementRepository.findAll();
    }

    @Override
    public Optional<Announcement> getAnnouncementById(final Long id) {
        return this.announcementRepository.findById(id);
    }

    @Override
    public Announcement saveAnnouncement(final Announcement announcement) {
        return this.announcementRepository.save(announcement);
    }

    @Override
    public void deleteAnnouncement(final Long id) {
        this.announcementRepository.deleteById(id);
    }

    @Override
    public List<Announcement> findTop3ByOrderByCreatedAtDesc() {
        return announcementRepository.findTop3ByOrderByCreatedAtDesc();
    }
}
