package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Door;
import com.example.entity.DoorResponse;
import com.example.entity.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/door")
public class DoorManageController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/open")
    public ResponseEntity<String> openDoor(@RequestBody Door openDoor) throws JsonProcessingException {
        String adminAuthority = openDoor.getAUTHORIZATION_TOKEN();
        String userId = openDoor.getUSER_ID();
        String doorUnion = openDoor.getDOOR_UNION();

        if (isValidUser(adminAuthority, userId) && isClose(doorUnion)) {
            String sql = "UPDATE cabinet_usage SET DOOR_STATUS = '1' WHERE DOOR_UNION = ?";
            try {
                // 执行修改
                int updatedRows = jdbcTemplate.update(sql, doorUnion);
                if (updatedRows > 0) {
                    // 创建状态对象并设置状态信息
                    State state = new State();
                    state.setstate_code(200);
                    state.setstate_msg("open successful");

                    // 构建响应对象
                    DoorResponse response = new DoorResponse();
                    response.setState(state);
                    response.setData("Door opened successfully");

                    // 将对象转换为 JSON 字符串
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonResponse = mapper.writeValueAsString(response);

                    return ResponseEntity.ok().body(jsonResponse);
                } else {
                    // 如果更新行数为0，则返回未找到数据的错误信息
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
                }
            } catch (DataAccessException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to open door");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process response");
            }
        } else {
            State state = new State();
            state.setstate_code(500);
            state.setstate_msg("open failed");

            // 构建响应对象
            DoorResponse response = new DoorResponse();
            response.setState(state);

            // 将对象转换为 JSON 字符串
            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(response);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
    }

    @PostMapping("/close")
    public ResponseEntity<String> closeDoor(@RequestBody Door closeDoor) throws JsonProcessingException {
        String adminAuthority = closeDoor.getAUTHORIZATION_TOKEN();
        String userId = closeDoor.getUSER_ID();
        String doorUnion = closeDoor.getDOOR_UNION();

        if (isValidUser(adminAuthority, userId) && isOpen(doorUnion)) {
            String sql = "UPDATE cabinet_usage SET DOOR_STATUS = '0' WHERE DOOR_UNION = ?";
            try {
                // 执行修改
                int updatedRows = jdbcTemplate.update(sql, doorUnion);
                if (updatedRows > 0) {
                    // 创建状态对象并设置状态信息
                    State state = new State();
                    state.setstate_code(200);
                    state.setstate_msg("close successful");

                    // 构建响应对象
                    DoorResponse response = new DoorResponse();
                    response.setState(state);
                    response.setData("Door closed successfully");

                    // 将对象转换为 JSON 字符串
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonResponse = mapper.writeValueAsString(response);

                    return ResponseEntity.ok().body(jsonResponse);
                } else {
                    // 如果更新行数为0，则返回未找到数据的错误信息
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
                }
            } catch (DataAccessException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to close door");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process response");
            }
        } else {
            State state = new State();
            state.setstate_code(404);
            state.setstate_msg("close failed");

            // 构建响应对象
            DoorResponse response = new DoorResponse();
            response.setState(state);

            // 将对象转换为 JSON 字符串
            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(response);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
    }

    // 验证用户身份的方法
    private boolean isValidUser(String authorizationToken, String userId) {
        // 当前登录的人 和 柜门对应的人 两个string都是账号
        String sql = "SELECT ADMIN_AUTORITY FROM user_information WHERE USER_ID = ?";
        Integer adminAuthority = jdbcTemplate.queryForObject(sql, Integer.class, authorizationToken);
        /*
         * 当前登陆的人权限是管理员或者是超级管理员可以通过或者当前登录的人是柜门对应的人也可以通过
         */
        return adminAuthority != null
                && (adminAuthority == 1 || adminAuthority == 2 || authorizationToken.equals(userId));
    }

    private boolean isClose(String doorUnion) {
        String sql = "SELECT DOOR_STATUS FROM cabinet_usage WHERE DOOR_UNION = ?";
        Integer doorStatus = jdbcTemplate.queryForObject(sql, Integer.class, doorUnion);
        /* 当前的状态 */
        return doorStatus != null && doorStatus == 0;
    }

    private boolean isOpen(String doorUnion) {
        String sql = "SELECT DOOR_STATUS FROM cabinet_usage WHERE DOOR_UNION = ?";
        Integer doorStatus = jdbcTemplate.queryForObject(sql, Integer.class, doorUnion);
        /* 当前的状态 */
        return doorStatus != null && doorStatus == 1;
    }
}
