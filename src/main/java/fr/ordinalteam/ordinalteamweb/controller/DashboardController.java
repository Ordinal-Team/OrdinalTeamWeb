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
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            final Optional<User> userOptional = this.userService.findByUsername(authentication.getName());
            if (userOptional.isPresent()) {
                final User user = userOptional.get();
                if (user.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMINISTRATOR || role.getName() == RoleName.MODERATOR)) {
                    return "dashboard";
                }
            }
        } else {
            return "redirect:/login";
        }
        return "redirect:/";
    }
}
