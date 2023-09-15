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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;


import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoListLoadingJrApplicationTests {
    @Autowired
    private TestRestTemplate test;

    @Test
    void shouldCreateATask(){
        Task ts = new Task(null,"first","description", Status.FINISHED, Instant.now());
        ResponseEntity<Void> reqTask = test.postForEntity("/tasks",ts, Void.class);
        assertThat(reqTask.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(reqTask.getHeaders().getLocation().getPath()).isNotEmpty();
    }
    @Test
    void shouldReturnRequestedTask(){
        ResponseEntity<String> response = test.getForEntity("/tasks/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer id = dc.read("id");
        assertThat(id).isEqualTo(1);

    }
    @DirtiesContext
    @Test
    void shouldReturnAllTasksSorted(){
        ResponseEntity<String> resp = test.getForEntity("/tasks", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext dc = JsonPath.parse(resp.getBody());
        JSONArray pageAmount = dc.read("$[*]");
        assertThat(pageAmount.size()).isEqualTo(2);

        int firstId = dc.read("$[0].id");
        assertThat(firstId).isEqualTo(1);
    }
    @DirtiesContext
    @Test
    void shouldUpdateATask(){
        test.put("/tasks/{id}",new Task(1012L,"testunitario","unitarioo",Status.FINISHED,Instant.now()),100L);
        ResponseEntity<String> resp = test.getForEntity("/tasks/{id}",String.class,100L);
        DocumentContext dc = JsonPath.parse(resp.getBody());
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        String title = dc.read("$.title");
        assertThat(title).isEqualTo("testunitario");
    }
    @Test
    void shouldDeleteATask(){
        ResponseEntity<String> respDel=test.exchange("/tasks/100", HttpMethod.DELETE,null,String.class);
        assertThat(respDel.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void shouldNotDeleteATask(){
        ResponseEntity<String> respDel=test.exchange("/tasks/20", HttpMethod.DELETE,null,String.class);
        assertThat(respDel.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldNotUpdateATask(){
        HttpEntity<Task> taskReq = new HttpEntity<>(new Task(1012L,"teste","unitarioo",Status.FINISHED,Instant.now()));
        ResponseEntity<String> resp= test.exchange("/tasks/2",HttpMethod.PUT,taskReq,String.class); //id 2 dont exists
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void shouldNotUpdateTaskByInvalidInput(){
        HttpEntity<Task> taskReq = new HttpEntity<>(null);
        ResponseEntity<String> resp= test.exchange("/tasks/1",HttpMethod.PUT,taskReq,String.class); //update id 1 with null values
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void shouldNotCreateATaskByInvalidInput(){
        Task ts = new Task(null,null,"description", null, Instant.now());
        ResponseEntity<Void> reqTask = test.postForEntity("/tasks",ts, Void.class);
        assertThat(reqTask.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(reqTask.getHeaders().getLocation()).isNull();
    }
}
