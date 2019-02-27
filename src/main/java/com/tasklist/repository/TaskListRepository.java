package com.tasklist.repository;

import com.tasklist.model.TaskList;
import org.springframework.data.repository.CrudRepository;

//@RepositoryRestResource(collectionResourceRel = "tasklist", path = "tasklist")
public interface TaskListRepository extends CrudRepository<TaskList, Long> {

}
