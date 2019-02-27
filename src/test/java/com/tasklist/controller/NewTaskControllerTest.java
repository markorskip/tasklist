package com.tasklist.controller;

import com.tasklist.model.User;
import com.tasklist.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NewTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User test_user;

    @Before
    public void setUp() throws Exception {
        test_user = new User("test_username123","testpasswd","name","email");
        userRepository.save(test_user);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteByUsername(test_user.getUsername());
    }

    @Test
    public void createNewTaskList() throws Exception {

        long numberofTasklists = test_user.getTasklists().size();

        mockMvc.perform(
                get("/api/users/"+test_user.getUsername()+"/tasklists/new"))
        .andExpect(status().isOk());

        //refresh the test_user
        test_user = userRepository.findByUsername(test_user.getUsername());
        long newNumberOfTaskLists = test_user.getTasklists().size();

        assert(newNumberOfTaskLists==numberofTasklists+1);


    }
}