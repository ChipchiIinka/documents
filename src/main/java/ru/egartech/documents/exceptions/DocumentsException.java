package ru.egartech.documents.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DocumentsException extends RuntimeException{
    private final ErrorType type;

    public DocumentsException(ErrorType type, String massage){
        super(massage);
        this.type = type;
    }

    public DocumentsException(ErrorType type, String massage, Throwable throwable){
        super(massage, throwable);
        this.type = type;
    }

    public DocumentsException(ErrorType type, Throwable throwable){
        super(throwable);
        this.type = type;
    }
}

