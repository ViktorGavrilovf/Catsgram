package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        if (existUser == null) throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        if (!users.containsKey(existUser.getId())) throw new DuplicatedDataException("Этот имейл уже используется");
        if (user.getUsername() != null) existUser.setUsername(user.getUsername());
        if (user.getPassword() != null) existUser.setPassword(user.getPassword());
        if (user.getEmail() != null) existUser.setEmail(user.getEmail());
        return existUser;
    }

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private long idGenerate() {
        long id = users.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);
        return ++id;
    }
}
