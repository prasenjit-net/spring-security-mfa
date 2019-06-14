package net.prasenjit.poc.springsecuritymfa.controller;

import lombok.RequiredArgsConstructor;
import net.prasenjit.poc.springsecuritymfa.model.MfaUser;
import net.prasenjit.poc.springsecuritymfa.service.MfaUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final MfaUserService mfaUserService;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("register")
    public String register() {
        return "register";
    }

    @PostMapping("register")
    public String registerDone(@ModelAttribute MfaUser user, Model model) {
        MfaUser user1 = mfaUserService.createUser(user.getUsername(), user.getPassword());
        model.addAttribute("user", user1);
        return "register-done";
    }
}
