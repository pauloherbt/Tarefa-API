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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



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
        assertThat(reqTask.getHeaders().getLocation().getPath()).isNotEmpty();
    }
    @Test
    void shouldReturnAllTasksSorted(){
        ResponseEntity<String> resp = test.getForEntity("/tasks", String.class);
        assertThat(resp.getStatusCode().equals(HttpStatus.OK));

        DocumentContext dc = JsonPath.parse(resp.getBody());
        JSONArray pageAmount = dc.read("$[*]");
        assertThat(pageAmount.size()).isEqualTo(1);

        int firstId = dc.read("$[0].id");
        assertThat(firstId).isEqualTo(1);
    }
    @Test
    void shouldReturnRequestedTask(){
        ResponseEntity<String> response = test.getForEntity("/tasks/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer id = dc.read("id");
        assertThat(id).isEqualTo(1);

    }
    @Test
    void shouldUpdateATask(){
        test.put("/tasks/{id}",new Task(1012L,"testunitario","unitarioo",Status.FINISHED),100L);
        ResponseEntity<String> resp = test.getForEntity("/tasks/{id}",String.class,100L);
        DocumentContext dc = JsonPath.parse(resp.getBody());
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        String title = dc.read("$.title");
        assertThat(title).isEqualTo("first");
    }

}
