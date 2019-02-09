package com.example.tasklist.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@NoArgsConstructor()
@Entity
@Table(name="task_list")
public class TaskList {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "tasklists")
    private Set<User> users = new HashSet<>();

    @Lob
    @Column
    private String jsonTaskList;

    public TaskList(String jsonTaskList) {
        this.jsonTaskList = jsonTaskList;
    }


}

