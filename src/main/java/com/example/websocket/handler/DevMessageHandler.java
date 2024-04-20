package com.example.websocket.handler;

import cn.hutool.json.JSONUtil;
import com.example.websocket.config.Dev2ServiceConfig;
import com.example.websocket.to.UnifiedTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * TODO
 *
 * @author mmciel 761998179@qq.com
 * @version 1.0.0
 * @date 2024/3/30 3:06
 * @update none
 */
@Service
@Slf4j
public class DevMessageHandler {

    @Autowired
    private ApplicationContext context;

    private Function<UnifiedTo, UnifiedTo> handleReqFrameFun;

    public DevMessageHandler() {
        handleReqFrameFun = req -> {
            Map<String, Dev2ServiceConfig.BeanInfo> conf = Dev2ServiceConfig.conf;
            Dev2ServiceConfig.BeanInfo beanInfo = conf.get(req.getReqKey());
            Class<?> beanClass = beanInfo.getBeanClass();
            String methodName = beanInfo.getMethodName();
            UnifiedTo r;
            Object bean = context.getBean(beanClass);
            try {
                Method method = beanClass.getMethod(methodName, UnifiedTo.class);
                r = (UnifiedTo) method.invoke(bean, req);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("消息分发发生错误,请检查Dev2ServiceConfig配置");
                throw new RuntimeException(e);
            }
            return r;
        };
    }

    public UnifiedTo handle(WebSocketSession session, String message) {

        UnifiedTo responseTo = null;

        UnifiedTo to = JSONUtil.toBean(message, UnifiedTo.class, true);
        log.info(to.toString());
        switch (UnifiedTo.parseReqType(to.getReqType())) {
            case ACK_TYPE:
                handleAckFrame(session, to);
                break;
            case HEART_FRAME:
                handleHeartFrame(session, to);
                break;
            case DATA_TYPE:
                // 任意数据帧必须给显式回复
                responseTo = handleReqFrame(session, to);
                break;
            default:
                log.error("帧类型错误");
        }
        return responseTo;
    }

    private UnifiedTo handleReqFrame(WebSocketSession session, UnifiedTo to) {
        log.info("============正式处理=============");
        Map<String, Dev2ServiceConfig.BeanInfo> conf = Dev2ServiceConfig.conf;
        // 确保请求能被分发器处理
        Optional.ofNullable(to.getReqKey())
                .filter(StringUtils::isNotBlank)
                .filter(conf::containsKey)
                .orElseThrow(() -> {
                    log.error("请求中的reqKey为空或者不能被分发器处理");
                    return new RuntimeException("请求中的reqKey为空或者不能被分发器处理");
                });
        UnifiedTo responseTo = handleReqFrameFun.apply(to);
        log.info("处理结果:{}", responseTo.toString());
        log.info("============处理结束=============");
        return responseTo;
    }

    private void handleHeartFrame(WebSocketSession session, UnifiedTo to) {
        log.info("处理心跳帧");
    }

    private void handleAckFrame(WebSocketSession session, UnifiedTo to) {
        log.info("业务自行处理ACK帧,参考数据帧即可");
    }

}
