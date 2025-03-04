package com.example.Task_Management.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
    }


    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNotFoundException(Throwable cause) {
        super(cause);
    }
}
