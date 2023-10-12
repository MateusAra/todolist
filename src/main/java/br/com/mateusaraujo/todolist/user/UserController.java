package br.com.mateusaraujo.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController 
{
    @Autowired
    private IUserRepository userRepository;

    public UserController(IUserRepository userRepository)
    {
        userRepository = this.userRepository;
    }

    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody UserModel user)
    {
        UserModel userExists = userRepository.findByUsername(user.getUsername());

        if (userExists != null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário já existe!");
        }

        var passwordEncrypt = BCrypt.withDefaults()
        .hashToString(12, user.getPassword().toCharArray());

        user.setPassword(passwordEncrypt);

        UserModel newUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newUser);
    }
}
