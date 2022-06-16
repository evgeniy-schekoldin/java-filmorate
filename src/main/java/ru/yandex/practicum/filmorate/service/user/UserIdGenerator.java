package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Component;

@Component
public class UserIdGenerator {

    private static long id;

    public long generate() {
        return ++id;
    }

}
