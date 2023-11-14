package org.emmek.beu2w3d2.services;

import org.emmek.beu2w3d2.config.EmailSender;
import org.emmek.beu2w3d2.entities.User;
import org.emmek.beu2w3d2.exception.BadRequestException;
import org.emmek.beu2w3d2.exception.UnauthorizedException;
import org.emmek.beu2w3d2.payloads.UserLoginDTO;
import org.emmek.beu2w3d2.payloads.UserPostDTO;
import org.emmek.beu2w3d2.repositories.UserRepository;
import org.emmek.beu2w3d2.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSender emailSender;

    @Autowired
    private UserService usersService;
    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

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
        if (bcrypt.matches(body.password(), user.getPassword())) {
            // 3. Se le credenziali sono OK --> Genero un JWT e lo restituisco
            return jwtTools.createToken(user);
        } else {
            // 4. Se le credenziali NON sono OK --> 401
            throw new UnauthorizedException("wrong password");
        }
    }

    public User registerUser(UserPostDTO body) throws IOException {
        userRepository.findByUsername(body.username()).ifPresent(a -> {
            throw new BadRequestException("Username " + a.getUsername() + " already exists");
        });
        userRepository.findByEmail(body.email()).ifPresent(a -> {
            throw new BadRequestException("User with email " + a.getEmail() + " already exists");
        });
        User user = new User();
        user.setUsername(body.username());
        user.setPassword(bcrypt.encode(body.password()));
        user.setName(body.name());
        user.setSurname(body.surname());
        user.setEmail(body.email());
        User savedUser = userRepository.save(user);
        emailSender.sendRegistrationEmail(savedUser);
        return savedUser;
    }
}