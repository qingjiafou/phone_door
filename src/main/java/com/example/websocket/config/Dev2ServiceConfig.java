package com.example.websocket.config;

import com.example.controller.DoorManageController;
import com.example.controller.personmanagecontroller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 将硬件消息映射为指定的Service中的方法
 *
 * @author mmciel 761998179@qq.com
 * @version 1.0.0
 * @date 2024/3/30 1:53
 * @update none
 */
@Data
@NoArgsConstructor
public class Dev2ServiceConfig {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BeanInfo {
        private Class<?> beanClass;
        private String methodName;
    }

    public static Map<String, BeanInfo> conf = new HashMap<>();
    static {
        conf.put("getAllUserData", new BeanInfo(personmanagecontroller.class, "getAllUserData"));
        conf.put("searchUserData", new BeanInfo(personmanagecontroller.class, "searchUserData"));
        conf.put("updateUserData", new BeanInfo(personmanagecontroller.class, "updateUserData"));
        conf.put("deleteUserData", new BeanInfo(personmanagecontroller.class, "deleteUserData"));
        conf.put("addUserData", new BeanInfo(personmanagecontroller.class, "addUserData"));
        conf.put("openDoor", new BeanInfo(DoorManageController.class, "openDoor"));
        conf.put("closeDoor", new BeanInfo(DoorManageController.class, "closeDoor"));
    }
}
