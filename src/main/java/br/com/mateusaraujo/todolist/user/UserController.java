package br.com.mateusaraujo.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController 
{
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/createUser")
    public UserModel createUser(@RequestBody UserModel user)
    {
        var newUser = this.userRepository.save(user);
        return newUser;
    }
}
