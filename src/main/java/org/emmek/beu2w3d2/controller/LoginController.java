package org.emmek.beu2w3d2.controller;

import org.emmek.beu2w3d2.entities.User;
import org.emmek.beu2w3d2.exception.BadRequestException;
import org.emmek.beu2w3d2.payloads.UserLoginDTO;
import org.emmek.beu2w3d2.payloads.UserLoginSuccessDTO;
import org.emmek.beu2w3d2.payloads.UserPostDTO;
import org.emmek.beu2w3d2.services.LoginService;
import org.emmek.beu2w3d2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginSuccessDTO login(@RequestBody @Validated UserLoginDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return new UserLoginSuccessDTO(loginService.authenticateUser(body));
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public User saveUser(@RequestBody @Validated UserPostDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            try {
                return userService.save(body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}