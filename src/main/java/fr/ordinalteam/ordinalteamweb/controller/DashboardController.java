package fr.ordinalteam.ordinalteamweb.controller;

import fr.ordinalteam.ordinalteamweb.model.RoleName;
import fr.ordinalteam.ordinalteamweb.model.User;
import fr.ordinalteam.ordinalteamweb.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String getDashboardView() {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        return "dashboard";
    }

    @GetMapping("/dashboard/members")
    public String getMembersView() {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        return "dashboard-members";
    }

    @GetMapping("/dashboard/plugins")
    public String getPluginsView() {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        return "dashboard-plugins";
    }

    @GetMapping("/dashboard/settings")
    public String getSettingsView() {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        return "dashboard-settings";
    }

    @GetMapping("/dashboard/announce/create")
    public String getAnnounceView() {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        return "dashboard-publish";
    }

    private boolean isAuth() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName());
    }

    private boolean isUserAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            final Optional<User> userOptional = this.userService.findByUsername(authentication.getName());
            if (userOptional.isPresent()) {
                final User user = userOptional.get();
                return user.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMINISTRATOR);
            }
        }
        return false;
    }
}
