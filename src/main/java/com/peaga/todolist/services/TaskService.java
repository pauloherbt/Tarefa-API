package com.peaga.todolist.services;

import com.peaga.todolist.entities.Task;
import com.peaga.todolist.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    public List<Task> findAll(){
        int pageSize=5;
        int pageNumber=0;
        Pageable page= PageRequest.of(pageNumber,pageSize, Sort.by("id").ascending());
        return taskRepository.findAll(page).getContent();
    }
    public Task findById(long id){
        return taskRepository.findById(id).orElseThrow();
    }
    public Task insert(Task task){
        return taskRepository.save(task);
    }
    public Task update(Long id,Task task){
        Task toUpdate = null;
        try{
            toUpdate = taskRepository.getReferenceById(id);
            if(task.getTitle()!=null)
                toUpdate.setTitle(task.getTitle());
            if(task.getDescription()!=null)
                toUpdate.setDescription((task.getDescription()));
            if(task.getStatus()!=null){
                toUpdate.setStatus(task.getStatus());
            }
        }
        catch (EntityNotFoundException e){
            e.printStackTrace();
        }
        return taskRepository.save(toUpdate);
    }


}
