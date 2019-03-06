package com.tasklist.model.projections;

import com.tasklist.model.TaskList;
import com.tasklist.model.User;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name="inlineUsers", types = {TaskList.class})
public interface InlineUsers {

    String getTaskListName();

    String getJsonTaskList();

    Set<User> getUsers();

    long getId();

}
