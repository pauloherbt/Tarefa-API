package com.peaga.todolist.resources;

import com.peaga.todolist.entities.Task;
import com.peaga.todolist.enums.Status;
import com.peaga.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Pageable pg = PageRequest.of(0,5,Sort.by("id").ascending());
        Page page = taskService.findAll(pg);
        return ResponseEntity.ok().body(page.getContent());
    }
    @GetMapping(value="/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long Id){
        return ResponseEntity.ok().body(taskService.findById(Id));
    }
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Task task,UriComponentsBuilder ucb){
        Task taskToSave = new Task(null,task.getTitle(),task.getDescription(), Status.valueOf(task.getStatus()));
        taskToSave = taskService.insert(taskToSave);
        URI uri = ucb.path("/tasks/{id}").buildAndExpand(taskToSave.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
