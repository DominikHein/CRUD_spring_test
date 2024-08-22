package com.example.my_first_rest_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    private ResponseEntity<User> register(@RequestBody User newUser){

        var repsonse = userRepository.save(newUser);

        return new ResponseEntity<User>(repsonse, HttpStatus.CREATED);
    }

    @GetMapping("/User")
    private ResponseEntity<User> getUserTodo(@RequestParam(value = "id") int id){

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()){
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }

        return new ResponseEntity("Fehler", HttpStatus.BAD_REQUEST);
    }


}
