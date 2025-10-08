package org.pucminas.assertj.model;

import java.util.UUID;

public class User {
    private UUID id;
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void generateId() {
        this.id = UUID.randomUUID();
    }
}
