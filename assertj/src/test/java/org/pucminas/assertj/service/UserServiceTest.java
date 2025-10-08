package org.pucminas.assertj.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucminas.assertj.model.User;
import org.pucminas.assertj.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void saveUser_withValidUser_returnsSavedUser() {
        var user = createUser();

        var savedUser = userService.save(user);

        assertAll(
                () -> assertThat(savedUser)
                        .extracting(User::getId)
                        .isNotNull(),
                () -> assertThat(savedUser.getLogin()).isEqualTo(user.getLogin()),
                () -> assertThat(savedUser.getPassword()).isEqualTo(user.getPassword())
        );
    }

    @Test
    void findById_withSavedUser_returnsUser() {
        var expectedUser = createUser();
        var id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(expectedUser);

        var result = userService.findById(id);

        assertThat(result).isEqualTo(expectedUser);
    }

    @Test
    void findById_withInvalidId_throwsIllegalArgumentException() {
        var id = UUID.randomUUID();

        when(userRepository.findById(id)).thenThrow(new IllegalArgumentException("Usuario nao encontrado para o ID: " + id));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.findById(id))
                .withMessage("Usuario nao encontrado para o ID: " + id);
    }

    @Test
    void findAll_withSavedUsers_returnsAllUsers() {
        var firstUser = createUser();
        var secondUser = createUser();
        when(userRepository.findAll()).thenReturn(List.of(firstUser, secondUser));

        var result = userService.findAll();

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(firstUser, secondUser);
    }

    @Test
    void findAll_withEmptySavedUsers_returnsEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        var result = userService.findAll();

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    private User createUser() {
        return new User("Gabriel", "gabriel@email.com");
    }
}
