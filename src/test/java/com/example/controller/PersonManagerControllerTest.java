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

import com.example.entity.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonManagerControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllUserData() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/person/all",
                String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateUserData() {
        // 构建请求体
        User updatedUser = new User();
        updatedUser.setUSER_ID("123"); // 设置需要更新的用户ID
        updatedUser.setUSER_NAME("丁七"); // 设置更新后的用户名
        updatedUser.setUSER_PASSWORD("123456"); // 设置更新后的用户密码
        updatedUser.setADMIN_AUTORITY(3); // 设置更新后的权限等级
        updatedUser.setDOOR_ID("3"); // 设置更新后的柜子编号
        updatedUser.setDOOR_NUMBER("5"); // 设置更新后的柜门编号
        updatedUser.setFACE_INFORMATION("New Name"); // 设置更新后的面部数据
        updatedUser.setFINGER_INFORMATION("New Name"); // 设置更新后的手指数据
        updatedUser.setDOOR_UNION("TEST"); // 设置更新后的面部数据

        // 发送POST请求
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/person/modify",
                updatedUser, Map.class);

        // 验证响应状态码
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testdeleteuserdata() {
        // 构建请求体
        User updatedUser = new User();
        updatedUser.setUSER_ID("13190");
        // 发送POST请求
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/person/delete",
                updatedUser, Map.class);

        // 验证响应状态码
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testserchuserdata() {
        // 构建请求体
        User updatedUser = new User();
        updatedUser.setUSER_ID("123");
        // 发送POST请求
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/person/search",
                updatedUser, Map.class);

        // 验证响应状态码
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testadduserdata() {
        // 构建请求体
        User updatedUser = new User();
        updatedUser.setUSER_ID("测试ID2"); // 设置需要更新的用户ID
        updatedUser.setUSER_NAME("测试"); // 设置更新后的用户名
        updatedUser.setUSER_PASSWORD("测试密码"); // 设置更新后的用户密码
        updatedUser.setADMIN_AUTORITY(1); // 设置更新后的权限等级
        updatedUser.setDOOR_ID("1"); // 设置更新后的柜子编号
        updatedUser.setDOOR_NUMBER("2"); // 设置更新后的柜门编号
        updatedUser.setFACE_INFORMATION("New FACE"); // 设置更新后的面部数据
        updatedUser.setFINGER_INFORMATION("New FINGER"); // 设置更新后的手指数据
        updatedUser.setDOOR_UNION("NEW_DOOR"); // 设置更新后的面部数据
        // 发送POST请求
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/person/add",
                updatedUser, Map.class);

        // 验证响应状态码
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}