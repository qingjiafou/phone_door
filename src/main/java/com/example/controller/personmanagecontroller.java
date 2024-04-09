package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.State;
import com.example.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/person")
public class personmanagecontroller {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * 展示所有成员信息
     */
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllUserData() {
        String sql = "SELECT * FROM user_information";
        List<Map<String, Object>> userData = jdbcTemplate.queryForList(sql);

        if (!userData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(userData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /*
     * 以用户的id进行搜索
     */
    @PostMapping("/search")
    public ResponseEntity<String> searchUserData(@RequestBody User updatedUser) {
        String sql = "select * FROM user_information WHERE USER_ID=?";
        try {
            // 执行查询
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, updatedUser.getUSER_ID());

            if (!result.isEmpty()) {
                // 创建状态对象并设置状态信息
                State state = new State();
                state.setstate_code(200);
                state.setstate_msg("search successful");

                // 将状态信息和查询结果添加到返回的 JSON 中
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("state", state);
                responseMap.put("data", result);

                String responseJson = objectMapper.writeValueAsString(responseMap);
                return ResponseEntity.status(HttpStatus.OK).body(responseJson);
            } else {
                // 如果结果为空，则返回未找到数据的错误信息
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Serialization failed");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to search data");
        }
    }

    /*
     * 修改信息
     */
    @PostMapping("/modify")
    public ResponseEntity<String> updateUserData(@RequestBody User updatedUser) {
        // 验证用户身份
        String authorizationToken = updatedUser.getAUTHORIZATION_TOKEN();
        if (!isValidUser(authorizationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
        String sql = "UPDATE user_information SET USER_NAME=?, FACE_INFORMATION=?, FINGER_INFORMATION=?, ADMIN_AUTORITY=?, DOOR_UNION=?, DOOR_ID=?, USER_PASSWORD=?, DOOR_NUMBER=? WHERE USER_ID=?";
        int rowsAffected = jdbcTemplate.update(sql,
                updatedUser.getUSER_NAME(),
                updatedUser.getFACE_INFORMATION(),
                updatedUser.getFINGER_INFORMATION(),
                updatedUser.getADMIN_AUTORITY(),
                updatedUser.getDOOR_UNION(),
                updatedUser.getDOOR_ID(),
                updatedUser.getUSER_PASSWORD(),
                updatedUser.getDOOR_NUMBER(),
                updatedUser.getUSER_ID()); // 将 USER_ID 作为参数传递到 SQL 语句中

        if (rowsAffected > 0) {
            // 创建状态对象并设置状态信息
            State state = new State();
            state.setstate_code(200);
            state.setstate_msg("Modification successful");
            saveLogToDatabase("修改身份信息", updatedUser.getAUTHORIZATION_TOKEN());
            // 将状态信息添加到返回的 JSON 中
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("state", state);
                responseMap.put("message", "Data updated successfully");

                String responseJson = objectMapper.writeValueAsString(responseMap);
                return ResponseEntity.status(HttpStatus.OK).body(responseJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("updated failed");
            }
        } else {
            // 更新失败，返回具体错误信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update data: No rows affected");
        }
    }
    /*
     * 删除信息
     */

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUserData(@RequestBody User updatedUser) {
        // 验证用户身份
        String authorizationToken = updatedUser.getAUTHORIZATION_TOKEN();
        if (!isValidUser(authorizationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
        String sql = "DELETE FROM user_information WHERE USER_ID=?";
        int rowsAffected = jdbcTemplate.update(sql, updatedUser.getUSER_ID());
        if (rowsAffected > 0) {
            // 创建状态对象并设置状态信息
            State state = new State();
            state.setstate_code(200);
            state.setstate_msg("Deletion successful");
            saveLogToDatabase("删除身份信息", updatedUser.getAUTHORIZATION_TOKEN());
            // 将状态信息添加到返回的 JSON 中
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("state", state);
                responseMap.put("message", "Data deleted successfully");

                String responseJson = objectMapper.writeValueAsString(responseMap);
                return ResponseEntity.status(HttpStatus.OK).body(responseJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Serialization failed");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete data");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> adddateUserData(@RequestBody User updatedUser) {
        // 验证用户身份
        String authorizationToken = updatedUser.getAUTHORIZATION_TOKEN();
        if (!isValidUser(authorizationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
        String sql = "insert into user_information  (USER_NAME, USER_ID, USER_PASSWORD, ADMIN_AUTORITY, DOOR_ID, DOOR_NUMBER, FACE_INFORMATION, FINGER_INFORMATION, DOOR_UNION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql,
                updatedUser.getUSER_NAME(),
                updatedUser.getUSER_ID(),
                updatedUser.getUSER_PASSWORD(),
                updatedUser.getADMIN_AUTORITY(),
                updatedUser.getDOOR_ID(),
                updatedUser.getDOOR_NUMBER(),
                updatedUser.getFACE_INFORMATION(),
                updatedUser.getFINGER_INFORMATION(),
                updatedUser.getDOOR_UNION());
        if (rowsAffected > 0) {
            // 创建状态对象并设置状态信息
            State state = new State();
            state.setstate_code(200);
            state.setstate_msg("Add successful");
            saveLogToDatabase("添加身份信息", updatedUser.getAUTHORIZATION_TOKEN());
            // 将状态信息添加到返回的 JSON 中
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("state", state);
                responseMap.put("message", "Data add successfully");

                String responseJson = objectMapper.writeValueAsString(responseMap);
                return ResponseEntity.status(HttpStatus.OK).body(responseJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("add failed");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to  data");
        }
    }

    // 验证用户身份的方法
    private boolean isValidUser(String authorizationToken) {
        // 当前登录的人
        String sql = "SELECT ADMIN_AUTORITY FROM user_information WHERE USER_ID = ?";
        Integer adminAuthority = jdbcTemplate.queryForObject(sql, Integer.class, authorizationToken);
        /*
         * 当前登陆的人权限是管理员或者是超级管理员可以通过
         */
        return adminAuthority != null
                && (adminAuthority == 1 || adminAuthority == 2);
    }

    private void saveLogToDatabase(String logMessage, String userId) {
        String sql = "INSERT INTO system_logs (log_message, time, USER_ID) VALUES (?, NOW(), ?)";
        jdbcTemplate.update(sql, logMessage, userId);
    }
}