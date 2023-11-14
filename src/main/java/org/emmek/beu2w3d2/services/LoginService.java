package org.emmek.beu2w3d2.services;

import org.emmek.beu2w3d2.entities.User;
import org.emmek.beu2w3d2.exception.UnauthorizedException;
import org.emmek.beu2w3d2.payloads.UserLoginDTO;
import org.emmek.beu2w3d2.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserService usersService;

    @Autowired
    private JWTTools jwtTools;

    public String authenticateUser(UserLoginDTO body) {
        User user;
        try {
            user = usersService.findByUsername(body.username());
        } catch (Exception e) {
            try {
                user = usersService.findByEmail(body.username());
            } catch (Exception ex) {
                throw new UnauthorizedException("username or email not found");
            }

        }
        if (body.password().equals(user.getPassword())) {
            // 3. Se le credenziali sono OK --> Genero un JWT e lo restituisco
            return jwtTools.createToken(user);
        } else {
            // 4. Se le credenziali NON sono OK --> 401
            throw new UnauthorizedException("Credenziali non valide!");
        }
    }
}