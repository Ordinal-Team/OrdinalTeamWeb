package fr.ordinalteam.ordinalteamweb.controller.admin;

import fr.ordinalteam.ordinalteamweb.dto.AnnouncementDTO;
import fr.ordinalteam.ordinalteamweb.model.Announcement;
import fr.ordinalteam.ordinalteamweb.model.Category;
import fr.ordinalteam.ordinalteamweb.model.RoleName;
import fr.ordinalteam.ordinalteamweb.model.User;
import fr.ordinalteam.ordinalteamweb.service.AnnouncementService;
import fr.ordinalteam.ordinalteamweb.service.CategoryService;
import fr.ordinalteam.ordinalteamweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final CategoryService categoryService;
    private final UserService userService;

    public AnnouncementController(AnnouncementService announcementService, CategoryService categoryService, UserService userService) {
        this.announcementService = announcementService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/dashboard/announce/create")
    public String getAnnounceView(Model model) {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("announcementDTO", new AnnouncementDTO());
        return "dashboard-publish";
    }

    @PostMapping("/dashboard/announce/publish")
    public String createAnnouncement(@Valid AnnouncementDTO announcementDTO, BindingResult result, Model model) {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "dashboard-publish";
        }

        Announcement announcement = new Announcement();
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setDescription(announcementDTO.getDescription());

        Optional<Category> categoryOptional = categoryService.getCategoryById(announcementDTO.getCategoryId());
        categoryOptional.ifPresent(announcement::setCategory);

        User currentUser = userService.getCurrentUser();
        announcement.setAuthor(currentUser);

        announcementService.saveAnnouncement(announcement);
        return "redirect:/dashboard/announce/create";
    }

    private boolean isAuth() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private boolean isUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            Optional<User> userOptional = userService.findByUsername(authentication.getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return user.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMINISTRATOR);
            }
        }
        return false;
    }
}
