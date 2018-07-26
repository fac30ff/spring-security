package sample.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sample.persistence.User;
import sample.persistence.UserRepository;

@RestController
@RequestMapping("/u")
public class DataController {

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/list")
    public Iterable<User> get() {
        return userRepository.findAll();
    }

    @PostMapping(value = "/list1")
    public Iterable<User> get1() {
        return userRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public User get(@PathVariable Long id) {
        return userRepository.findById(id).orElse(User.Empty());
    }

    @PostMapping(value = "/add")
    public User add(@RequestBody User user) {
        return userRepository.save(user);
    }
}
