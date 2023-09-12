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
        return ResponseEntity.ok().body(taskService.findAll());
    }
    @GetMapping(value="/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(taskService.findById(id));
    }
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Task task,UriComponentsBuilder ucb){
        Task taskToSave = new Task(null,task.getTitle(),task.getDescription(),Status.FINISHED);
        taskToSave = taskService.insert(taskToSave);
        URI uri = ucb.path("/tasks/{id}").buildAndExpand(taskToSave.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Task task){
        taskService.update(id,task);
        return ResponseEntity.ok().build();
    }

}
