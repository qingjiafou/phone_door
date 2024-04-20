package com.example.controller;

import java.net.http.WebSocket;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.State;
import com.example.entity.User;
import com.example.websocket.to.UnifiedTo;

@RestController
@RequestMapping("/person")
public class personmanagecontroller {

    @Autowired
    WebSocket defaultWebSocket;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * 展示所有成员信息
     */
    @GetMapping("/all")
    public UnifiedTo getAllUserData() {
        String sql = "SELECT * FROM user_information";
        try {
            List<Map<String, Object>> userData = jdbcTemplate.queryForList(sql);

            UnifiedTo unifiedTo = new UnifiedTo();
            if (!userData.isEmpty()) {
                // 查询成功
                unifiedTo.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
                State state = new State();
                state.setstate_code(200);
                state.setstate_msg("Data retrieved successfully");
                unifiedTo.setState(state);
                unifiedTo.setReqPayload(userData);
            } else {
                // 查询结果为空
                unifiedTo.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
                State state = new State();
                state.setstate_code(404);
                state.setstate_msg("No data found");
                unifiedTo.setState(state);
            }
            return unifiedTo;
        } catch (DataAccessException e) {
            // 查询过程中出现异常
            UnifiedTo unifiedTo = new UnifiedTo();
            unifiedTo.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            State state = new State();
            state.setstate_code(500);
            state.setstate_msg("Failed to retrieve data");
            unifiedTo.setState(state);
            return unifiedTo;
        }
    }

    /*
     * 以用户的id进行搜索
     */
    @PostMapping("/search")
    public UnifiedTo searchUserData(@RequestBody User updatedUser) {
        String sql = "SELECT * FROM user_information WHERE USER_ID=?";
        try {
            // 执行查询
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, updatedUser.getUSER_ID());

            UnifiedTo unifiedTo = new UnifiedTo();
            if (!result.isEmpty()) {
                // 创建状态对象并设置状态信息
                State state = new State();
                state.setstate_code(200);
                state.setstate_msg("Search successful");

                // 设置状态和查询结果到 UnifiedTo 对象中
                unifiedTo.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
                unifiedTo.setState(state);
                unifiedTo.setReqPayload(result);
            } else {
                // 如果结果为空，则返回未找到数据的错误信息
                unifiedTo.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
                State state = new State();
                state.setstate_code(404);
                state.setstate_msg("No data found");
                unifiedTo.setState(state);
            }
            return unifiedTo;
        } catch (DataAccessException e) {
            // 查询过程中出现异常
            UnifiedTo unifiedTo = new UnifiedTo();
            unifiedTo.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            State state = new State();
            state.setstate_code(500);
            state.setstate_msg("Failed to search data");
            unifiedTo.setState(state);
            return unifiedTo;
        }
    }

    /*
     * 修改信息
     */
    @PostMapping("/modify")
    public UnifiedTo updateUserData(@RequestBody User updatedUser) {
        // 验证用户身份
        String authorizationToken = updatedUser.getAUTHORIZATION_TOKEN();
        if (!isValidUser(authorizationToken)) {
            UnifiedTo unauthorizedResponse = new UnifiedTo();
            unauthorizedResponse.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            State unauthorizedState = new State();
            unauthorizedState.setstate_code(401);
            unauthorizedState.setstate_msg("Unauthorized access");
            unauthorizedResponse.setState(unauthorizedState);
            return unauthorizedResponse;
        }

        String sql = "UPDATE user_information SET USER_NAME=?, FACE_INFORMATION=?, FINGER_INFORMATION=?, ADMIN_AUTORITY=?, DOOR_UNION=?, DOOR_ID=?, USER_PASSWORD=?, DOOR_NUMBER=? WHERE USER_ID=?";
        try {
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

            UnifiedTo response = new UnifiedTo();
            response.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            if (rowsAffected > 0) {
                // 更新成功
                State state = new State();
                state.setstate_code(200);
                state.setstate_msg("Modification successful");
                response.setState(state);
                saveLogToDatabase("修改身份信息", authorizationToken);
            } else {
                // 更新失败，返回具体错误信息
                State state = new State();
                state.setstate_code(500);
                state.setstate_msg("Failed to update data: No rows affected");
                response.setState(state);
            }
            return response;
        } catch (DataAccessException e) {
            // 更新过程中出现异常
            UnifiedTo errorResponse = new UnifiedTo();
            errorResponse.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            State errorState = new State();
            errorState.setstate_code(500);
            errorState.setstate_msg("Failed to update data: Database error");
            errorResponse.setState(errorState);
            return errorResponse;
        }
    }
    /*
     * 删除信息
     */

    @PostMapping("/delete")
    public UnifiedTo deleteUserData(@RequestBody User updatedUser) {
        // 验证用户身份
        String authorizationToken = updatedUser.getAUTHORIZATION_TOKEN();
        if (!isValidUser(authorizationToken)) {
            UnifiedTo unauthorizedResponse = new UnifiedTo();
            unauthorizedResponse.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            State unauthorizedState = new State();
            unauthorizedState.setstate_code(401);
            unauthorizedState.setstate_msg("Unauthorized access");
            unauthorizedResponse.setState(unauthorizedState);
            return unauthorizedResponse;
        }
        String sql = "DELETE FROM user_information WHERE USER_ID=?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, updatedUser.getUSER_ID());
            UnifiedTo response = new UnifiedTo();
            response.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            if (rowsAffected > 0) {
                // 删除成功
                State state = new State();
                state.setstate_code(200);
                state.setstate_msg("Deletion successful");
                response.setState(state);
                saveLogToDatabase("删除身份信息", authorizationToken);
            } else {
                // 删除失败，返回具体错误信息
                State state = new State();
                state.setstate_code(500);
                state.setstate_msg("Failed to delete data");
                response.setState(state);
            }
            return response;
        } catch (DataAccessException e) {
            // 删除过程中出现异常
            UnifiedTo errorResponse = new UnifiedTo();
            errorResponse.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            State errorState = new State();
            errorState.setstate_code(500);
            errorState.setstate_msg("Failed to delete data: Database error");
            errorResponse.setState(errorState);
            return errorResponse;
        }
    }

    @PostMapping("/add")
    public UnifiedTo addUserData(@RequestBody User updatedUser) {
        // 验证用户身份
        String authorizationToken = updatedUser.getAUTHORIZATION_TOKEN();
        if (!isValidUser(authorizationToken)) {
            UnifiedTo unauthorizedResponse = new UnifiedTo();
            unauthorizedResponse.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            State unauthorizedState = new State();
            unauthorizedState.setstate_code(401);
            unauthorizedState.setstate_msg("Unauthorized access");
            unauthorizedResponse.setState(unauthorizedState);
            return unauthorizedResponse;
        }
        String sql = "INSERT INTO user_information (USER_NAME, USER_ID, USER_PASSWORD, ADMIN_AUTORITY, DOOR_ID, DOOR_NUMBER, FACE_INFORMATION, FINGER_INFORMATION, DOOR_UNION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
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
            UnifiedTo response = new UnifiedTo();
            response.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            if (rowsAffected > 0) {
                // 添加成功
                State state = new State();
                state.setstate_code(200);
                state.setstate_msg("Add successful");
                response.setState(state);
                saveLogToDatabase("添加身份信息", authorizationToken);
            } else {
                // 添加失败，返回具体错误信息
                State state = new State();
                state.setstate_code(500);
                state.setstate_msg("Failed to add data");
                response.setState(state);
            }
            return response;
        } catch (DataAccessException e) {
            // 添加过程中出现异常
            UnifiedTo errorResponse = new UnifiedTo();
            errorResponse.setReqType(UnifiedTo.ReqType.ACK_TYPE.value);
            State errorState = new State();
            errorState.setstate_code(500);
            errorState.setstate_msg("Failed to add data: Database error");
            errorResponse.setState(errorState);
            return errorResponse;
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