package com.peaga.todolist.services.exceptions;

public class IllegalInputException extends RuntimeException{
    public IllegalInputException(String msg) {
        super(msg);
    }
}
