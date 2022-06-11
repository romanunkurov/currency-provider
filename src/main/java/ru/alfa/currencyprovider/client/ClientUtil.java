package ru.alfa.currencyprovider.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

public class ClientUtil {

    /**
     * Формирование общего ответа при Fallback
     *
     * @param exception Исключение, которое вызвало Fallback
     * @param body      Сопровождающий объект при ответе сервера
     * @return Ответ сервера
     */
    public static <T> ResponseEntity<T> getFallbackResponseEntity(Throwable exception, T body) {
        HttpStatus status = exception instanceof HttpStatusCodeException
            ? ((HttpStatusCodeException) exception).getStatusCode()
            : HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
            .status(status)
            .body(body);
    }
}
