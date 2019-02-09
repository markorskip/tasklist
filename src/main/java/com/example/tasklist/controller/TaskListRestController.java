package com.example.tasklist.controller;

import com.example.tasklist.model.TaskList;
import com.example.tasklist.model.User;
import com.example.tasklist.repository.TaskListRepository;
import com.example.tasklist.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Optional;

@RestController()
@RequestMapping(path="/tasklist",produces = "application/json")
@CrossOrigin(origins = "*")
public class TaskListRestController {

    @Resource
    private TaskListRepository taskListRepository;

    @Resource
    private UserRepository userRepository;

    @GetMapping("{id}")
    public ResponseEntity<TaskList> taskListById(@PathVariable("id") Long id) {
        TaskList taskList = taskListRepository.findById(id).get();
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @PutMapping(value = "{id}", consumes = "application/json")
    public TaskList putTaskList(@RequestBody String taskListJson, @PathVariable("id") Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(authentication.getPrincipal());
//        User thisuser = (User) authentication.getPrincipal();



        //System.out.println(thisuser.getUsername());
        System.out.println(taskListJson);
        System.out.println(id);

        //Create a new tasklist and assign it the json and if
        TaskList taskList = new TaskList();
        taskList.setId(id);
        taskList.setJsonTaskList(taskListJson);

        // Update the many to many relationship
        HashSet<TaskList> taskLists = new HashSet<>();
        taskLists.add(taskList);
        //User user = userRepository.findByUsername(thisuser.getUsername());
        //user.setTasklists(taskLists);

        //will update if exists
        taskListRepository.save(taskList);

        //This updates the join table
        //userRepository.save(user);

        return taskList;
    }

    @GetMapping("/")
    public ResponseEntity<String> defaultResponse() {
        StringBuilder all = new StringBuilder();
        Iterable<TaskList> allTaskLists =  taskListRepository.findAll();

        for (TaskList taskList1: allTaskLists ) {
            all.append(taskList1.toString());
        }
        return new ResponseEntity<>(all.toString(), HttpStatus.OK);
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }



}
