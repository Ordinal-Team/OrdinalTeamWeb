package fr.ordinalteam.ordinalteamweb.controller.admin;

import fr.ordinalteam.ordinalteamweb.model.RoleName;
import fr.ordinalteam.ordinalteamweb.model.User;
import fr.ordinalteam.ordinalteamweb.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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
    public String getMembersView(final Model model) {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        List<User> users = this.userService.findAll();
        model.addAttribute("users", users);
        return "dashboard-members";
    }

    @PostMapping("/dashboard/members/edit")
    public String editUser(final User user) {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        this.userService.updateUserDetails(user);
        return "redirect:/dashboard/members";
    }

    @PostMapping("/dashboard/members/toggle-email-verified")
    public String toggleEmailVerified(@RequestParam("id") Long id, @RequestParam("status") boolean status) {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        final User user = this.userService.findById(id);
        if (user != null) {
            this.userService.updateEmailVerified(id, status);
        }

        return "redirect:/dashboard/members";
    }

    @PostMapping("/dashboard/members/toggle-two-factor")
    public String toggleTwoFactor(@RequestParam("id") Long id, @RequestParam("status") boolean status) {
        final User user = this.userService.findById(id);
        if (user != null) {
            this.userService.updateTwoFactorEnabled(id, status);
        }
        return "redirect:/dashboard/members";
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
