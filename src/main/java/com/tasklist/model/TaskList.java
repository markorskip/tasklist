package com.tasklist.model;

import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="task_list")
@RestResource(rel="tasklists",path="tasklists")
public class TaskList {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tasklists")
    private Set<User> users = new HashSet<>();

    @Column(length = 1000000)
    private String jsonTaskList;

    public TaskList(String jsonTaskList) {
        this.jsonTaskList = jsonTaskList;
    }

    public TaskList() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getJsonTaskList() {
        return jsonTaskList;
    }

    public void setJsonTaskList(String jsonTaskList) {
        this.jsonTaskList = jsonTaskList;
    }


    // If we print ths users it can create an infinite loop......
    @Override
    public String toString() {
        return "TaskList{" +
                "id=" + id +
                ", jsonTaskList='" + jsonTaskList + '\'' +
                '}';
    }
}

