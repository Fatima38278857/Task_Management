package com.example.Task_Management.exception;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException() {
    }


    public NotFoundCommentException(String message) {
        super(message);
    }

    public NotFoundCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCommentException(Throwable cause) {
        super(cause);
    }
}
