package br.com.mateusaraujo.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity createUser(@RequestBody UserModel user)
    {
        UserModel userExists = this.userRepository.findByUsername(user.getUsername());

        if (userExists != null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário já existe!");
        }

        UserModel newUser = this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newUser);
    }
}
