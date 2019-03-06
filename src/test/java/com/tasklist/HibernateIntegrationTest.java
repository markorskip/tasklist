package com.tasklist;

import com.tasklist.model.TaskList;
import com.tasklist.model.User;
import com.tasklist.repository.TaskListRepository;
import com.tasklist.repository.UserRepository;

import javafx.concurrent.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HibernateIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskListRepository taskListRepository;

    @Test
    public void testManyToMany() {
        System.out.println("Testing Many to Many relationships");

        //Test respository for non existent user
        User nullUser= userRepository.findByUsername("asdcdscasdcasd_nulll_asdfasd");
        assert(nullUser==null);


        String test_username="test_username5";
        User test_user = userRepository.findByUsername(test_username);
        if (test_user!=null) {
            System.out.println("Deleting test user before creating");
            userRepository.deleteById(test_username);
        }

        //Count the user and tasks table before creating
        long userCount = userRepository.count();
        long tasklistCount = taskListRepository.count();

        System.out.println("User Count: " + userCount);
        System.out.println("TaskList Count: " + tasklistCount);

        System.out.println("Creating test user");
        test_user = new User(test_username,"test_password","test_name","test_email");
        userRepository.save(test_user);
        test_user = userRepository.findByUsername(test_username);

        System.out.println("Test User:");
        System.out.println(test_user);

        System.out.println("Adding tasks");
        TaskList taskList1 = new TaskList("test json string 1 ABCDEFG");
        TaskList taskList2 = new TaskList("test json string 2");
        test_user.getTasklists().add(taskList1);
        test_user.getTasklists().add(taskList2);

        //Save the user
        System.out.println("Before save: " + test_user);
        test_user = userRepository.save(test_user);

        //Assert the tasklist was saved
        for (TaskList taskList: test_user.getTasklists()) {
            System.out.println(taskList);
            Optional<TaskList> foundTaskList = taskListRepository.findById(taskList.getId());
            assert(foundTaskList.isPresent());

        }

        //Assert one user was saved and two tasks were created
        assert(userRepository.count()==userCount+1);
        assert(taskListRepository.count()==tasklistCount+2);

        //Clean up - delete the user
        userRepository.deleteById(test_user.getUsername());

        for (TaskList taskList: test_user.getTasklists()) {
            System.out.println(taskList);
            taskListRepository.delete(taskList);
        }

       //Assert user does not exist, user deleted, and tasks deleted
        assert(userRepository.findByUsername(test_username)==null);
        assert(userRepository.count()==userCount);

        //Assert the associated tasks were deleted
        System.out.println(tasklistCount);
        System.out.println(taskListRepository.count());
        assert(taskListRepository.count()==tasklistCount);
    }

}


