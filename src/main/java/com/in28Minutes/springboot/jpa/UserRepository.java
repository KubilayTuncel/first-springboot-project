package com.in28Minutes.springboot.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path="users",collectionResourceRel = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List <User> findByRole(String role);
}
