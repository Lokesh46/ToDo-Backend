package com.in28minutes.rest.webservices.restfulwebservices.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in28minutes.rest.webservices.restfulwebservices.todo.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer>{
	
	List<Todo> findByUsername(String username);

	Optional<Todo> findByIdAndUsername(int id, String username);

	List<Todo> findByUsernameAndDone(String username, Boolean done);


}
