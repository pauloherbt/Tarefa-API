package com.peaga.todolist.services;

import com.peaga.todolist.entities.Task;
import com.peaga.todolist.repositories.TaskRepository;
import com.peaga.todolist.services.exceptions.DbException;
import com.peaga.todolist.services.exceptions.IllegalInputException;
import com.peaga.todolist.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    public List<Task> findAll(int pageNumber){
        int pageSize=1;
        Pageable page= PageRequest.of(pageNumber,pageSize, Sort.by("id").ascending());
        return taskRepository.findAll(page).getContent();
    }
    public Task findById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }
    public Task insert(Task task){
        Task tsk =null;
        try{
            task.setDate(Instant.now());
            tsk=taskRepository.save(task);
        }
        catch (DataIntegrityViolationException e){
            throw new DbException(e.getMessage());
        }
        return tsk;
    }
    public Task update(Long id, Task task){
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
            if(task.getTitle()==null||task.getDescription()==null||task.getStatus()==null) {
                throw new IllegalInputException("Invalid Input");
            }
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
        return taskRepository.save(toUpdate);
    }
    public void delete(Long id){
        Task tsk = null;
        try{
             tsk = findById(id);
             taskRepository.delete(tsk);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException dt){
            throw new DbException(dt.getMessage());
        }
    }

}
