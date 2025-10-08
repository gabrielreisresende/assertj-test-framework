package org.pucminas.assertj.repository;

import org.pucminas.assertj.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository {

    private List<User> users = new ArrayList<>();

    public User save(User user) {
        user.generateId();
        users.add(user);
        return user;
    }

    public User findById(UUID id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado para o ID: " + id));
    }

    public List<User> findAll(){
        return users;
    }
}
