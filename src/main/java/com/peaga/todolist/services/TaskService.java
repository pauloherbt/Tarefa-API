package com.peaga.todolist.services;

import com.peaga.todolist.entities.Task;
import com.peaga.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    public List<Task> findAll(){
        return taskRepository.findAll();
    }
    public Task findById(long id){
        return taskRepository.findById(id).orElseThrow();
    }
    public void insert(Task task){
        taskRepository.save(task);
    }


}
