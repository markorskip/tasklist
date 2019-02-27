package com.tasklist.repository;

import com.tasklist.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

//@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends CrudRepository<User, String> {

    User findByUsername(String username);

    @Transactional
    void deleteByUsername(String username);

}
