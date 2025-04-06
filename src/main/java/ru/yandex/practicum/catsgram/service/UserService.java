package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new ConditionsNotMetException("Имейл должен быть указан");
        if (users.containsValue(user))
            throw new DuplicatedDataException("Этот имейл уже используется");
        user.setId(idGenerate());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
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
