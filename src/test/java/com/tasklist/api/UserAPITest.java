package com.tasklist.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tasklist.model.TaskList;
import com.tasklist.model.User;
import com.tasklist.repository.UserRepository;
import com.tasklist.security.SecurityConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URI;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User test_user;

    @Before
    public void setUp() throws Exception {
        test_user = new User("username_test123","password","name","email");
        if (userRepository.findByUsername(test_user.getUsername())==null) {
            userRepository.save(test_user);
        } else {
            System.out.println("User already created");
        }

    }

    @After
    public void tearDown() {
            userRepository.deleteByUsername(test_user.getUsername());
    }

    //@WithMockUser("/skip")
    @Test
    public void testGetRequest() throws Exception {
        this.mockMvc.perform(get("/api/users")).andExpect(status().isOk());
    }

    @Test
    public void testCreateDeleteNewUser() throws Exception {
        String username = "mock_mvc_test_username";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        //Create JSON object
        jsonNode.put("username", username);
        jsonNode.put("password", "password");
        jsonNode.put("name", "name");
        jsonNode.put("email", "email");

        String jsonStr= objectMapper.writeValueAsString(jsonNode);

        System.out.println(jsonStr);

        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonStr))
        .andExpect(status().isCreated());

        mockMvc.perform(get("/api/users/"+username+"/tasklists/"))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/api/users/"+username))
                .andExpect(status().isNoContent());

    }


    @Test
    public void testCreateManyToManyRelationship() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode tasklistJsonNode = objectMapper.createObjectNode();

        tasklistJsonNode.put("jsonTaskList","{testing json tasklist rest endpoint}");
        String tasklistJsonStr = objectMapper.writeValueAsString(tasklistJsonNode);
        System.out.println(tasklistJsonStr);

        // Create new tasklist, return the uri
        MvcResult result = mockMvc.perform(post("/api/tasklists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tasklistJsonStr))
                .andExpect(status().isCreated())
                .andReturn();

        String uri = result.getResponse().getRedirectedUrl();
        System.out.println("PRINTING response: "  + uri);

        // POST the uri to the user to create an association
        mockMvc.perform(post("/api/users/"+test_user.getUsername()+"/tasklists")
                .contentType("text/uri-list")
                .content(uri))
                .andExpect(status().isNoContent());

    }

}
