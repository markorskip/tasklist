package com.tasklist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasklist.model.TaskList;
import com.tasklist.model.User;
import com.tasklist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@BasePathAwareController
public class NewTaskController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/users/{user}/tasklists/new")
    public @ResponseBody ResponseEntity<?> createNewTaskList(@PathVariable("user") String user) {  //@RequestBody TaskList taskList @RequestBody binds json to a java object automatically
        System.out.println("Attempting to create a new tasklist for user: " + user);
        User userToUpdate = userRepository.findByUsername(user);
        if (userToUpdate!= null) {
            TaskList taskList = new TaskList("{\"label\":\"New tasklist\"}");
            userToUpdate.getTasklists().add(taskList);
            userRepository.save(userToUpdate);
            System.out.println(userRepository.findByUsername(userToUpdate.getUsername()));
            return ResponseEntity.ok("Tasklist created");
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/testing")
    public @ResponseBody ResponseEntity<?> testing() {
        List<String> list = new ArrayList<>();
        list.add("test");
        Resources<String> resources = new Resources<>(list);
        return ResponseEntity.ok(resources);
    }
}
