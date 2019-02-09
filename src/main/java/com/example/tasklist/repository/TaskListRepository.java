package com.example.tasklist.repository;

import com.example.tasklist.model.TaskList;
import org.springframework.data.repository.CrudRepository;

public interface TaskListRepository extends CrudRepository<TaskList, Long> {

}
