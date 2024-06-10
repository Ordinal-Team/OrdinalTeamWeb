package fr.ordinalteam.ordinalteamweb.controller;

import fr.ordinalteam.ordinalteamweb.model.User;
import fr.ordinalteam.ordinalteamweb.service.AnnouncementService;
import fr.ordinalteam.ordinalteamweb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final UserService userService;
    private final AnnouncementService announcementService;

    public HomeController(final UserService userService, final AnnouncementService announcementService) {
        this.userService = userService;
        this.announcementService = announcementService;
    }

    @GetMapping("/")
    public String home(final Model model) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        logger.info("Home page accessed");

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            logger.info("User is authenticated: {}", authentication.getName());
            final Optional<User> userOptional = this.userService.findByUsername(authentication.getName());

            if (userOptional.isPresent()) {
                final User user = userOptional.get();
                logger.info("User found: {}", user.getUsername());
                user.getRoles().forEach(r -> logger.info("Role: {}", r.getName()));
                model.addAttribute("user", user);
            } else {
                logger.warn("User not found: {}", authentication.getName());
            }
        } else {
            logger.info("User is not authenticated or is anonymous");
            model.addAttribute("user", null);
        }
        model.addAttribute("announcements", this.announcementService.findTop3ByOrderByCreatedAtDesc());

        return "index";
    }
}
