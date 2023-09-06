package com.peaga.todolist.resources;

import com.peaga.todolist.entities.Task;
import com.peaga.todolist.enums.Status;
import com.peaga.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskResource {
    private TaskService taskService;

    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping
    public ResponseEntity<List<Task>> findAll(){
        return ResponseEntity.ok().body(taskService.findAll());
    }
    @GetMapping(value="/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long Id){
        return ResponseEntity.ok().body(taskService.findById(Id));
    }
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Task task,UriComponentsBuilder ucb){
        Task taskToSave = new Task(null,task.getTitle(),task.getDescription(),task.getDate(), Status.valueOf(task.getStatus()));
        taskToSave = taskService.insert(taskToSave);
        URI uri = ucb.path("/tasks/{id}").buildAndExpand(taskToSave.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
