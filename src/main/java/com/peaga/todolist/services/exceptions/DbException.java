package com.peaga.todolist.services.exceptions;

public class DbException extends RuntimeException{
    public DbException(String msg) {
        super(msg);
    }
}
