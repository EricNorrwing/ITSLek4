package se.norrwing.itslek4demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PageController {

    @GetMapping("/user/home")
    public String homePage () {
        return "home";
    }

    @GetMapping("/admin/home")
    public String adminHome () {
        return "adminHome";
    }
}
