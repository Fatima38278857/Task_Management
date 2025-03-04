package com.example.Task_Management.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Role {
    AUTHOR, EXECUTOR
}
