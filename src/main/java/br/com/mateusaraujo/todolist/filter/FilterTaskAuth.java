package br.com.mateusaraujo.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.mateusaraujo.todolist.user.IUserRepository;
import br.com.mateusaraujo.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authorization = request.getHeader("Authorization");
        var authEncoded = authorization.substring("Basic".length()).trim();

        byte[] authDecode = Base64.getDecoder().decode(authEncoded); 
        var auth = new String(authDecode);

        String[] credentials = auth.split(":");
        UserModel user = this.userRepository.findByUsername(credentials[0]);

        if (user == null)
        {
            response.sendError(401);
        }
        else
        {
            var passwordVerify = BCrypt.verifyer().verify(credentials[1].toCharArray(), user.getPassword());

            if(passwordVerify.verified)
            {
                filterChain.doFilter(request, response);
            }
            else
            {
                response.sendError(401);
            }
        }
    }
}
