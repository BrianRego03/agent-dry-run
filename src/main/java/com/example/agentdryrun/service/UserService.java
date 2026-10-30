package com.example.agentdryrun.service;

import com.example.agentdryrun.entity.User;
import com.example.agentdryrun.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(String name, String email, String password) {
        User newUser = new User(name, email, password);
        return userRepository.save(newUser);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public String deleteUser(int id) {
        userRepository.deleteById(id);
        return "Success";
    }

    public List<User> getAllUsers(String sort){
        if (sort == null || sort.isBlank()) {
            return userRepository.findAll();
        }
        return userRepository.findAll(Sort.by(sort));
    }


}
