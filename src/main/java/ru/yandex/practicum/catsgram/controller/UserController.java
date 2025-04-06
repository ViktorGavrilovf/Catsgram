package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new ConditionsNotMetException("Имейл должен быть указан");
        if (users.containsValue(user))
            throw new DuplicatedDataException("Этот имейл уже используется");
        user.setId(idGenerate());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() == null) throw new ConditionsNotMetException("Id должен быть указан");
        User existUser = users.get(user.getId());
        if (!users.containsKey(existUser.getId())) throw new DuplicatedDataException("Этот имейл уже используется");
        User oldUser = new User();
        if (user.getEmail() != null && user.getUsername() != null && user.getPassword() != null) {
            oldUser.setId(user.getId());
            oldUser.setUsername(user.getUsername());
            oldUser.setEmail(user.getEmail());
            oldUser.setPassword(user.getPassword());
            oldUser.setRegistrationDate(user.getRegistrationDate());
        }
        return oldUser;
    }

    private long idGenerate() {
        long id = users.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);
        return ++id;
    }
}
