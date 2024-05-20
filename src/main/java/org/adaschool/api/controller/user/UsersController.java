package org.adaschool.api.controller.user;

import org.adaschool.api.exception.UserNotFoundException;
import org.adaschool.api.repository.user.User;
import org.adaschool.api.repository.user.UserDto;
import org.adaschool.api.service.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users/")
public class UsersController {

    private final UsersService usersService;

    public UsersController(@Autowired UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User user = this.usersService.save(new User(userDto));
        return ResponseEntity.created(null).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.usersService.all();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        Optional<User> optionalUser = this.usersService.findById(id);
        if (optionalUser.isEmpty()) throw new UserNotFoundException(id);
        return ResponseEntity.ok(optionalUser.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        Optional<User> optionalUser = this.usersService.findById(id);
        if (optionalUser.isEmpty()) throw new UserNotFoundException(id);
        User user = optionalUser.get();
        user.update(userDto);
        this.usersService.save(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        Optional<User> optionalUser = this.usersService.findById(id);
        if (optionalUser.isEmpty()) throw new UserNotFoundException(id);
        this.usersService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
