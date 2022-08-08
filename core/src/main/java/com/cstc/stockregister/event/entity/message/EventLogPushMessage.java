package com.cstc.stockregister.event.entity.message;

import com.cstc.stockregister.util.JsonUtils;
import lombok.Data;
import org.fisco.bcos.sdk.model.EventLog;

import java.util.List;

@Data
public class EventLogPushMessage implements MQObject  {

    /**
     * application which register block notify
     */
    private String appId;

    /**
     * event type: 1: blockNotify, 2: eventLogPush, 3: others
     */
    private Integer eventType;

    private Integer groupId;

    /**
     * related with
     */
    private int status;

    private List<EventLog> logs;

    @Override
    public String toString() {
        return JsonUtils.toJSONString(this);
    }

}
