package ru.egartech.documents.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    COMMON_ERROR("Ошибка бизнесс логики", "Повторите запрос позже"),
    NOT_FOUND("Не удалось найти ресурс", "По вашему запросу ресурс не найден"),
    CLIENT_ERROR("Ошибка в запросе", "Проверьте параметры и повторите запрос"),
    DB_ERROR("Ошибка базы данных", "Возникла проблема с базой данных"),
    MUST_BE_SAME("Разные файлы", "У обновляемого файла должно быть такое же название"),
    TOO_BIG("Слишком большой вес файла", "Вес файла не должен привышать 100мб"),
    ALREADY_EXISTS("Ошибка уникальности", "Файл с таким именем уже существует"),
    NOT_VALID_NAME("Неверное имя файла", "Имя файла не должно содержать знаки < > : \" / \\ | ? * ;");

    private final String title;
    private final String text;
}
