package org.study.todoapi.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message) {
        super(message);
    }
}
