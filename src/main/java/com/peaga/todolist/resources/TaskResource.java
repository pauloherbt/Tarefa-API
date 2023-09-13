package com.peaga.todolist.resources;

import com.peaga.todolist.entities.Task;
import com.peaga.todolist.enums.Status;
import com.peaga.todolist.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskResource {
    private TaskService taskService;

    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping()
    public ResponseEntity<List<Task>> findAll(){
        int pageNumber=0;
        return ResponseEntity.ok().body(taskService.findAll(pageNumber));
    }
    @GetMapping(value="/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(taskService.findById(id));
    }
    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task,UriComponentsBuilder ucb){
        Task taskToSave = new Task(null,task.getTitle(),task.getDescription(),Status.FINISHED,task.getDate());
        taskToSave = taskService.insert(taskToSave);
        URI uri = ucb.path("/tasks/{id}").buildAndExpand(taskToSave.getId()).toUri();
        return ResponseEntity.created(uri).body(taskToSave);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task){
        Task tsk= taskService.update(id,task);
        return ResponseEntity.ok(tsk);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        taskService.delete(id);
        return ResponseEntity.ok().build();
    }
}
