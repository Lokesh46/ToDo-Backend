package com.in28minutes.rest.webservices.restfulwebservices.todo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.rest.webservices.restfulwebservices.todo.repository.TodoRepository;
import com.in28minutes.rest.webservices.restfulwebservices.todo.repository.UserRepository;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;

@RestController
public class TodoJpaResource {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoJpaResource(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/users/{username}/todos")
    public ResponseEntity<List<Todo>> retrieveTodos(
            @PathVariable String username,
            @RequestParam(required = false) Boolean done) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();

        List<Todo> todos;
        if (done != null) {
            todos = todoRepository.findByUserAndDone(user, done);
        } else {
            todos = todoRepository.findByUser(user);
        }

        return ResponseEntity.ok(todos);
    }

    @GetMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Todo> retrieveTodo(@PathVariable String username,
                                             @PathVariable int id) {

        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty() || !optionalTodo.get().getUser().getUsername().equals(username)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalTodo.get());
    }

    @DeleteMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String username,
                                           @PathVariable int id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);

        if (optionalTodo.isEmpty() || !optionalTodo.get().getUser().getUsername().equals(username)) {
            return ResponseEntity.notFound().build();
        }

        todoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String username,
                                           @PathVariable int id,
                                           @RequestBody Todo todo) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        todo.setId(id);
        todo.setUser(userOptional.get());
        return ResponseEntity.ok(todoRepository.save(todo));
    }

    @PostMapping("/users/{username}/todos")
    public ResponseEntity<Todo> createTodo(@PathVariable String username,
                                           @RequestBody Todo todo) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        todo.setId(null);
        todo.setUser(userOptional.get());
        todo.setCompleteDate(null); // Ensure completeDate is null on creation
        return ResponseEntity.ok(todoRepository.save(todo));
    }

    @PutMapping("/users/{username}/todos/update/{id}")
    public ResponseEntity<Todo> markCompleteTodo(@PathVariable String username,
                                                 @PathVariable int id) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Todo existingTodo = optionalTodo.get();
        if (!existingTodo.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        existingTodo.setDone(true);
        existingTodo.setCompleteDate(LocalDate.now());
        return ResponseEntity.ok(todoRepository.save(existingTodo));
    }
}
