package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.entity.Door;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DoorManagerControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testopendoor() {
        // 构建请求体
        Door openDoor = new Door();
        openDoor.setDOOR_UNION("1-2");
        openDoor.setUSER_ID("123");
        openDoor.setAUTHORIZATION_TOKEN("123");
        openDoor.setDOOR_STATUS(0);

        // 发送POST请求
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/door/open",
                openDoor, Map.class);

        // 验证响应状态码
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testclosedoor() {
        // 构建请求体
        Door closeDoor = new Door();
        closeDoor.setDOOR_UNION("1-2");
        closeDoor.setUSER_ID("123");
        closeDoor.setAUTHORIZATION_TOKEN("123");
        closeDoor.setDOOR_STATUS(1);

        // 发送POST请求
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/door/close",
                closeDoor, Map.class);

        // 验证响应状态码
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
