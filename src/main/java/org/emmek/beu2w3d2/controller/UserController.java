package org.emmek.beu2w3d2.controller;

import org.emmek.beu2w3d2.entities.User;
import org.emmek.beu2w3d2.exception.BadRequestException;
import org.emmek.beu2w3d2.exception.NotFoundException;
import org.emmek.beu2w3d2.payloads.UserPostDTO;
import org.emmek.beu2w3d2.payloads.UserPutDTO;
import org.emmek.beu2w3d2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sort) {
        return userService.getUsers(page, size, sort);
    }

    @GetMapping("/me")
    public UserDetails getProfile(@AuthenticationPrincipal UserDetails currentUser) {
        return currentUser;
    }

    @PutMapping("/me")
    public UserDetails getProfile(@AuthenticationPrincipal User currentUser, @RequestBody UserPutDTO body) {
        return userService.findByIdAndUpdate(currentUser.getId(), body);
    }

    @PostMapping("/me/avatar")
    public String uploadExample(@AuthenticationPrincipal User currentUser, @RequestParam("avatar") MultipartFile body) throws IOException {
        return userService.uploadPicture(currentUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- 204 NO CONTENT
    public void getProfile(@AuthenticationPrincipal User currentUser) {
        userService.findByIdAndDelete(currentUser.getId());
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public User postUsers(@RequestBody @Validated UserPostDTO body, BindingResult validation) {
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

    @PostMapping("/{id}/avatar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String uploadExample(@PathVariable long id, @RequestParam("avatar") MultipartFile body) throws IOException {
        return userService.uploadPicture(id, body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable long id) {
        try {
            return userService.findById(id);
        } catch (Exception e) {
            throw new NotFoundException(id);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findByIdAndUpdate(@PathVariable long id, @RequestBody @Validated UserPutDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return userService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable int id) {
        try {
            userService.findByIdAndDelete(id);
        } catch (Exception e) {
            throw new NotFoundException(id);
        }
    }

}
