package com.peaga.todolist.services;

import com.peaga.todolist.entities.Task;
import com.peaga.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    public Page<Task> findAll(Pageable page){
        return taskRepository.findAll(page);
    }
    public Task findById(long id){
        return taskRepository.findById(id).orElseThrow();
    }
    public Task insert(Task task){
        return taskRepository.save(task);
    }


}
