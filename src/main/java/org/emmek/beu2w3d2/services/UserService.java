package org.emmek.beu2w3d2.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.emmek.beu2w3d2.config.EmailSender;
import org.emmek.beu2w3d2.entities.User;
import org.emmek.beu2w3d2.exception.NotFoundException;
import org.emmek.beu2w3d2.payloads.UserPutDTO;
import org.emmek.beu2w3d2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private EmailSender emailSender;

    public Page<User> getUsers(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userRepository.findAll(pageable);
    }

    public User findById(long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        userRepository.delete(user);
    }

    public User findByIdAndUpdate(long id, UserPutDTO body) throws NotFoundException {
        User u = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        u.setName(body.name());
        u.setSurname(body.surname());
        u.setEmail(body.email());
        return userRepository.save(u);
    }

    public String uploadPicture(long id, MultipartFile file) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        user.setAvatar(url);
        userRepository.save(user);
        return url;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with username " + username + " not found!"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email + " not found!"));
    }
}

