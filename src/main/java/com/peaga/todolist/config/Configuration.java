package com.peaga.todolist.config;

import com.peaga.todolist.entities.Task;
import com.peaga.todolist.enums.Status;
import com.peaga.todolist.repositories.TaskRepository;
import com.peaga.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@org.springframework.context.annotation.Configuration
@Profile("test")
public class Configuration implements CommandLineRunner {
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public void run(String... args) throws Exception {
        Task tsk1 = new Task(null,"titulo","description", Status.FINISHED);
        taskRepository.save(tsk1);
    }
}
