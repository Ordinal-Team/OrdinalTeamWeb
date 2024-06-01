package fr.ordinalteam.ordinalteamweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String getDashboardView() {
        return "dashboard";
    }
}
