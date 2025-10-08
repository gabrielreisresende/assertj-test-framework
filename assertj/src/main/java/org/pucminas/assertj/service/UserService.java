package org.pucminas.assertj.service;

import org.pucminas.assertj.model.User;
import org.pucminas.assertj.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {

    private UserRepository userRepository = new UserRepository();

    public User save(User user) {
        user.generateId();
        userRepository.save(user);
        return user;
    }

    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
