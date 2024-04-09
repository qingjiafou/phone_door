package com.example.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.entity.User;

import jakarta.annotation.Resource;

@Controller
@RequestMapping("/jdbc")
public class jdbccontroller {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private List<User> fetchUserList() {
        String sql = "SELECT * FROM user_information";
        return jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setUSER_NAME(rs.getString("USER_NAME"));
                user.setFACE_INFORMATION(rs.getString("FACE_INFORMATION"));
                user.setFINGER_INFORMATION(rs.getString("FINGER_INFORMATION"));
                user.setADMIN_AUTORITY(rs.getInt("ADMIN_AUTORITY"));
                user.setDOOR_UNION(rs.getString("DOOR_UNION"));
                user.setDOOR_ID(rs.getString("DOOR_ID"));
                user.setUSER_ID(rs.getString("USER_ID"));
                user.setUSER_PASSWORD(rs.getString("USER_PASSWORD"));
                user.setDOOR_NUMBER(rs.getString("DOOR_NUMBER"));
                return user;
            }
        });
    }

    @RequestMapping("/user")
    @ResponseBody
    public List<User> userListJson() {
        return fetchUserList();
    }

    @RequestMapping("/userList")
    public String userListPage(ModelMap map) {
        List<User> userList = fetchUserList();
        map.addAttribute("users", userList);
        return "user"; // 假设"user"是视图的名称
    }
}
