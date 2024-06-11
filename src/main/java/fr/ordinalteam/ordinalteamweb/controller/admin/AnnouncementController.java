package fr.ordinalteam.ordinalteamweb.controller.admin;

import fr.ordinalteam.ordinalteamweb.dto.AnnouncementDTO;
import fr.ordinalteam.ordinalteamweb.dto.CategoryDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
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
        List<Category> categories = this.categoryService.getAllCategories();
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
            List<Category> categories = this.categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "dashboard-publish";
        }

        final Announcement announcement = new Announcement();
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setDescription(announcementDTO.getDescription());

        final Optional<Category> categoryOptional = this.categoryService.getCategoryById(announcementDTO.getCategoryId());
        categoryOptional.ifPresent(announcement::setCategory);

        final User currentUser = userService.getCurrentUser();
        announcement.setAuthor(currentUser);

        this.announcementService.saveAnnouncement(announcement);
        return "redirect:/dashboard/announce/create";
    }

    @GetMapping("/dashboard/announce/create-category")
    public String showCreateCategoryPage(Model model) {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "dashboard-create-category";
    }

    @PostMapping("/dashboard/announce/create-category")
    public String createCategory(@Valid CategoryDTO categoryDTO, BindingResult result, Model model) {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            return "dashboard-create-category";
        }

        final Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        this.categoryService.saveCategory(category);

        return "redirect:/dashboard/announce/create";
    }

    @GetMapping("/dashboard/announce/manage")
    public String manageAnnouncementsAndCategories(Model model) {
        if (!isAuth()) {
            return "redirect:/login";
        } else if (!isUserAdmin()) {
            return "redirect:/";
        }

        List<Announcement> announcements = this.announcementService.findAll();
        List<Category> categories = this.categoryService.getAllCategories();

        model.addAttribute("announcements", announcements);
        model.addAttribute("categories", categories);
        return "dashboard-manage-announcements";
    }

    @GetMapping("/dashboard/announce/edit/{id}")
    public String editAnnouncement(@PathVariable Long id, Model model) {
        // Implement edit functionality
        return "redirect:/dashboard/announce/manage";
    }

    @GetMapping("/dashboard/announce/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return "redirect:/dashboard/announce/manage";
    }

    @GetMapping("/dashboard/announce/edit-category/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        // Implement edit functionality
        return "redirect:/dashboard/announce/manage";
    }

    @GetMapping("/dashboard/announce/delete-category/{id}")
    public String deleteCategory(@PathVariable Long id) {
        this.categoryService.deleteCategory(id);
        return "redirect:/dashboard/announce/manage";
    }

    private boolean isAuth() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private boolean isUserAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            final Optional<User> userOptional = this.userService.findByUsername(authentication.getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return user.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMINISTRATOR);
            }
        }
        return false;
    }
}
