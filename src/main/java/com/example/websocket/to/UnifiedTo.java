package com.example.websocket.to;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import com.example.entity.State;

/**
 * TODO
 *
 * @author mmciel 761998179@qq.com
 * @version 1.0.0
 * @date 2024/3/30 1:23
 * @update none
 */

@Data
@ToString
@Slf4j
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UnifiedTo {

    private String serviceId;
    private String deviceId;
    private String sender;
    private Long ts;
    private Integer seq;
    private String devCls;
    private Object devConf;
    private String reqType;
    private String reqKey;
    private String reqData;
    private Object reqPayload;
    private String reqIp;
    private State state;

    /**
     * {
     * "serviceId":"0", // 服务器ID，本项目只有一个服务器，可以写0
     * "deviceId": "100010", // 设备ID
     * "sender":"", // 发送者，设备发送填设备ID，服务器发送填服务器ID
     * "ts": 1711732404351, // 毫米级别的设备端时间戳
     * "seq": 100, // 请求序号,用于排序请求
     * "devCls": "box", // 设备类型 ,冗余设计
     * "devConf": {}, // 设备当前配置 ,冗余设计
     * "reqType": "data", // 请求类别 【心跳帧: heart】 【交互帧：data】 【确认帧：ack】
     * "reqKey": "xxx", // 请求归属的业务,用于调用对应的service
     * "reqData": "", // 请求携带的字符串参数,用于简单场景
     * "reqPayload": {}, // 请求携带的对象参数,用于复杂场景
     * "reqIp": "127.0.0.1" // 请求者IP
     * }
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public enum ReqType {
        HEART_FRAME("heart"), DATA_TYPE("data"), ACK_TYPE("ack");

        public String value;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public static ReqType parseReqType(String reqType) {
        if (StringUtils.isBlank(reqType)) {
            log.error("帧类型错误");
            throw new RuntimeException("帧类型错误");
        }
        if (ReqType.HEART_FRAME.value.equals(reqType)) {
            return ReqType.HEART_FRAME;
        } else if (ReqType.DATA_TYPE.value.equals(reqType)) {
            return ReqType.DATA_TYPE;
        } else if (ReqType.ACK_TYPE.value.equals(reqType)) {
            return ReqType.ACK_TYPE;
        } else {
            log.error("帧类型错误");
            throw new RuntimeException("帧类型错误");
        }
    }
}
