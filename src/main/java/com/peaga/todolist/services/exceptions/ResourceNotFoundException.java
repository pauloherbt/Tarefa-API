package com.peaga.todolist.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(Object id) {
        super("Resource Not Found Exception by id:"+id);
    }
}
