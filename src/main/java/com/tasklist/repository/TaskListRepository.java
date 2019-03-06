package com.tasklist.repository;

import com.tasklist.model.TaskList;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//@RepositoryRestResource(collectionResourceRel = "tasklist", path = "tasklist")
@Repository
public interface TaskListRepository extends CrudRepository<TaskList, Long> {

    @Transactional
    TaskList getById(long id);

    @Query("Select t from TaskList t JOIN FETCH t.users where t.id=(:id)")
    public TaskList customFetch(@Param("id") Long id);

}
