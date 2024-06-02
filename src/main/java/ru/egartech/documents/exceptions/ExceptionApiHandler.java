package ru.egartech.documents.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.egartech.documents.exceptions.baseresponse.BaseResponseService;
import ru.egartech.documents.exceptions.baseresponse.ResponseWrapper;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionApiHandler {
    private final BaseResponseService responseService;

    @ExceptionHandler(Throwable.class)
    public ResponseWrapper<?> handleOtherException(Throwable t) {
        log.error("Got exception {}, message: {}", t.getClass(), t.getMessage());
        return responseService.wrapErrorResponse(new DocumentsException(ErrorType.COMMON_ERROR, t));
    }

    @ExceptionHandler(DocumentsException.class)
    public ResponseWrapper<?> handleDocumentsException(DocumentsException exception) {
        return responseService.wrapErrorResponse(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseWrapper<?> handleValidationException(Exception e) {
        log.error("Got validation exception {}, message: {}", e.getClass(), e.getMessage());
        return responseService.wrapErrorResponse(new DocumentsException(ErrorType.CLIENT_ERROR, e));
    }

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseWrapper<?> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.error("File upload error, big weight: {}", e.getMessage(), e);
        return responseService.wrapErrorResponse(new DocumentsException(ErrorType.TOO_BIG, e));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseWrapper<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Throwable rootCause = e.getRootCause();
        if (rootCause instanceof SQLException sqlException && "23505".equals(sqlException.getSQLState())) {
                log.error("Data integrity violation: {}", sqlException.getMessage(), sqlException);
                String message = "Ошибка: попытка добавить запись с уже существующим ключом.";
                return responseService.wrapErrorResponse(new DocumentsException(ErrorType.ALREADY_EXISTS, message, e));
        }
        log.error("Data integrity violation: {}", e.getMessage(), e);
        return responseService.wrapErrorResponse(new DocumentsException(ErrorType.DB_ERROR, e));
    }
}
