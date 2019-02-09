package com.example.tasklist.controller;


import com.example.tasklist.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class FrontEndController {

    @Resource
    UserRepository userRepository;

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/main")
    public String getDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        model.addAttribute("user", userRepository.findByUsername("skip"));
        return "main";
    }

}
