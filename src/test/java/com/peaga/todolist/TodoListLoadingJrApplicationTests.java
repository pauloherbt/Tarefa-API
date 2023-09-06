package com.peaga.todolist;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.peaga.todolist.entities.Task;
import com.peaga.todolist.enums.Status;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoListLoadingJrApplicationTests {
    @Autowired
    private TestRestTemplate test;

    @Test
    void shouldCreateATask(){
        Task ts = new Task(null,"first","description", Status.FINISHED);
        ResponseEntity<Void> reqTask = test.postForEntity("/tasks",ts, Void.class);
        assertThat(reqTask.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(reqTask.getHeaders().getLocation().getPath()).isEqualTo("/tasks/1");
    }
    @Test
    void shouldReturnAllTasks(){
        ResponseEntity<String> resp = test.getForEntity("/tasks", String.class);
        assertThat(resp.getStatusCode().equals(HttpStatus.OK));
        DocumentContext dc = JsonPath.parse(resp.getBody());
        JSONArray pageAmount = dc.read("$[*]");
        assertThat(pageAmount.size()).isEqualTo(1);

    }

}
