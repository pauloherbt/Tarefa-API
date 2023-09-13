package com.peaga.todolist.resources.exceptions;

import com.peaga.todolist.services.exceptions.DbException;
import com.peaga.todolist.services.exceptions.IllegalInputException;
import com.peaga.todolist.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest httpRequest){
        String errorMsg = "Resource Not Found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError defaultError = new StandardError(Instant.now(),status.value(),errorMsg,e.getMessage(),httpRequest.getRequestURI());
        return ResponseEntity.status(status).body(defaultError);
    }
    @ExceptionHandler(IllegalInputException.class)
    public ResponseEntity<StandardError> inputException(IllegalInputException e,HttpServletRequest httpRequest){
        String errorMsg = "Illegal input data";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(), status.value(),errorMsg,e.getMessage(),httpRequest.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
    @ExceptionHandler(DbException.class)
    public ResponseEntity<StandardError> dbException(DbException e, HttpServletRequest request){
        String errorMsg = "DB Integrity Violation";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(), status.value(),errorMsg,e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status.value()).body(standardError);
    }
}
