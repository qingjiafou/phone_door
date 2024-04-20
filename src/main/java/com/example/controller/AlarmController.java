package com.example.controller;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.State;
import com.example.entity.UserTime;
import com.example.websocket.to.UnifiedTo;
import com.example.websocket.ws.WebSocket;

@RestController
public class AlarmController {
    @Autowired
    WebSocket defaultWebSocket;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/alarm")
    public UnifiedTo alarmtime(@RequestBody UserTime usertime) {
        String doorid = usertime.getDOOR_ID();
        String doornumber = usertime.getDOOR_NUMBER();
        Date nowtime = usertime.getnow_time();
        String sql = "SELECT END_TIME FROM cabinet_usage where DOOR_ID=? AND DOOR_NUMBER=?";
        Date endTime = jdbcTemplate.queryForObject(sql, Date.class, doorid, doornumber);
        try {
            UnifiedTo response = new UnifiedTo();
            State state = new State();
            if (nowtime.after(endTime)) {
                // 如果实际时间晚于查询时间，则返回状态和信息
                state.setstate_code(10);
                state.setstate_msg("超时");
            } else {
                state.setstate_code(200);
                state.setstate_msg("准时");
            }
            response.setState(state);
            return response;
        } catch (DataAccessException e) {
            // 处理数据库访问异常
            State state = new State();
            state.setstate_code(500);
            state.setstate_msg("数据库访问异常");
            UnifiedTo response = new UnifiedTo();
            response.setState(state);
            return response;
        }
    }
}