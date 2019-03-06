package com.tasklist.controller;


import com.tasklist.model.TaskList;
import com.tasklist.model.User;
import com.tasklist.repository.TaskListRepository;
import com.tasklist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*")
public class FrontEndController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskListRepository taskListRepository;

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/tasklist/{id}")
    public String getMain(@PathVariable("id") long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println(authentication.getName());
        User user = userRepository.findByUsername(username);
        Optional<TaskList> optionalTaskList = taskListRepository.findById(id);

        if (!optionalTaskList.isPresent()) {
            model.addAttribute("error","Tasklist with id: " + id + " not found");
            return "error";
        }

        if (user != null) {
            TaskList taskList = optionalTaskList.get();
            model.addAttribute("user", userRepository.findByUsername(username));
            model.addAttribute("taskListId",taskList.getId());
            model.addAttribute("taskList", taskList);
            return "tasklist";
        }
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if (user != null) {
                model.addAttribute("user", user);
                //TODO change this soo tasklists is just a collection of tasklists

                List<String> taskListList = new ArrayList<>();

                for (TaskList taskList : user.getTasklists()) {
                    taskListList.add(taskList.toString());
                }

                model.addAttribute("tasklists", taskListList);
                return "dashboard";
            }
        }  return "redirect:/login";
    }


}
