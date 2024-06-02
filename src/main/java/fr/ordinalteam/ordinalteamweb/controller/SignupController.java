package fr.ordinalteam.ordinalteamweb.controller;

import fr.ordinalteam.ordinalteamweb.dto.RegisterRequest;
import fr.ordinalteam.ordinalteamweb.dto.Response;
import fr.ordinalteam.ordinalteamweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signup(@ModelAttribute("registerRequest") RegisterRequest registerRequest) {
        return "signup";
    }

    @PostMapping
    public String registerUser(final @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please try again.");
            return "redirect:/signup";
        }

        final Response response = this.userService.registerUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getConfirmPassword());

        if (!response.isSuccess()) {
            redirectAttributes.addFlashAttribute("errorMessage", response.getErrorMessage());
            return "redirect:/signup";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! You can now login.");
        return "redirect:/login";
    }
}
