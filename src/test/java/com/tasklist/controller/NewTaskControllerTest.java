package com.tasklist.controller;

import com.tasklist.model.TaskList;
import com.tasklist.model.User;
import com.tasklist.repository.TaskListRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NewTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    private User test_user;
    private User test_user2;

    @Before
    public void setUp() throws Exception {
        test_user = new User("test_username123","testpasswd","name","email");
        test_user = userRepository.save(test_user);

        test_user2 = new User("test_user2_abc123456","","","");
        test_user2 = userRepository.save(test_user2);

        System.out.println("Before class set up two users");
        System.out.println(test_user);
        System.out.println(test_user2);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteByUsername(test_user.getUsername());
        userRepository.deleteByUsername(test_user2.getUsername());
    }

    @Test
    public void createNewTaskList() throws Exception {

        long numberofTasklists = test_user.getTasklists().size();

        System.out.println("****TEST USER BEFORE NEW*********");
        System.out.println(test_user);

        MvcResult result = mockMvc.perform(
                get("/api/users/"+test_user.getUsername()+"/tasklists/new"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("****TEST USER AFTER NEW*********");
        System.out.println(test_user);
        System.out.println(result.getResponse().toString());

        //refresh the test_user
        test_user = userRepository.findByUsername(test_user.getUsername());
        System.out.println("AFTER REFRESH******");
        System.out.println(test_user);
        long newNumberOfTaskLists = test_user.getTasklists().size();

        assert(newNumberOfTaskLists==numberofTasklists+1);



    }

    @Test
    public void manyToManyTest() {
        //Create a new tasklist
        TaskList testTaskList = new TaskList("test");
        testTaskList = taskListRepository.save(testTaskList);

        // Save to user 1
        test_user.getTasklists().add(testTaskList);
        test_user = userRepository.save(test_user);

        //Save to user 2
        test_user2.getTasklists().add(testTaskList);
        test_user2 = userRepository.save(test_user2);

        // Refresh the tasklist
        Optional<TaskList> opt_testTaskList = taskListRepository.findById(testTaskList.getId());
        testTaskList = opt_testTaskList.get();
        Set<User> userSet = testTaskList.getUsers();

        // Make sure the tasklist is associated with two users
        assert(userSet.size()==2);

        // Clean up
        taskListRepository.delete(testTaskList);

    }

    @Test
    public void deleteTaskList() throws Exception {
        String urlTemplate;

        //Create a new tasklist
        TaskList testTaskList = new TaskList("test");
        testTaskList = taskListRepository.save(testTaskList);

        //Save to user 1
        test_user.getTasklists().add(testTaskList);
        test_user = userRepository.save(test_user);

        //Save to user 2
        test_user2.getTasklists().add(testTaskList);
        test_user2 = userRepository.save(test_user2);

        //Refresh tasklist
        Optional<TaskList> opt_tasklist = taskListRepository.findById(testTaskList.getId());
        testTaskList = opt_tasklist.get();
        Set<User> userSet = testTaskList.getUsers();


        System.out.println("TESTING BEGIN");
        System.out.println(test_user);
        System.out.println(test_user2);
        System.out.println(testTaskList);
        System.out.println(userSet);
        System.out.println(userSet.contains(test_user));
        System.out.println(userSet.contains(test_user2));
        System.out.println(userSet);
        System.out.println("Hashcode for test_user: " + test_user.hashCode());
        for (User user : userSet) {
            System.out.println(user.hashCode());
        }


        assert(test_user.getTasklists().toString().contains("id="+testTaskList.getId()));
        assert(test_user2.getTasklists().toString().contains("id="+testTaskList.getId()));
        assert(userSet.size()==2);


        long tasklist_id = testTaskList.getId();
        System.out.println("Tasklist id: " + tasklist_id);

        // Test the deletion of association
        urlTemplate = "/api/users/"+test_user.getUsername()+"/tasklists/del/"+tasklist_id;
        mockMvc.perform(delete(urlTemplate))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Association removed")));
        System.out.println("Assocation deleted test passed");

        //refresh user, assert the first user no longer has the tasklist but the second one does
        System.out.println();
        test_user = userRepository.findByUsername(test_user.getUsername());
        test_user2 = userRepository.findByUsername(test_user2.getUsername());
        assert(!test_user.getTasklists().toString().contains("id="+testTaskList.getId()));
        assert(test_user2.getTasklists().toString().contains("id="+testTaskList.getId()));

        System.out.println("Tasklist userset:" + testTaskList.getUsers());

        // Test the user cannot delete once the association is done
        mockMvc.perform(delete(urlTemplate))
                .andExpect(status().isUnauthorized());
        System.out.println("Unauthorized deletion test passed");

        // Tasklist still exists
        assertTrue(taskListRepository.findById(tasklist_id)!=null);
        System.out.println("Tasklist still exists test passed");

        urlTemplate = "/api/users/"+test_user2.getUsername()+"/tasklists/del/"+tasklist_id;

        //Test Expect is ok and deleted if only one user has the tasklist
        mockMvc.perform(delete(urlTemplate))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("TaskList deleted")))
                .andReturn();

        //Assert the tasklist has actually been deleted
        assert(!taskListRepository.findById(tasklist_id).isPresent());

        //Expect not found when there is no id
        mockMvc.perform(delete(urlTemplate))
                .andExpect(status().isNotFound());

    }

}