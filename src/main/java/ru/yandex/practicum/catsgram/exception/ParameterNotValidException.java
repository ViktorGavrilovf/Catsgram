package ru.yandex.practicum.catsgram.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParameterNotValidException extends IllegalArgumentException {
    String parameter;
    String reason;
}
