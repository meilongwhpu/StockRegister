package com.cstc.stockregister.event.callback;

import com.cstc.stockregister.event.MQPublisher;
import com.cstc.stockregister.event.entity.message.EventLogPushMessage;
import com.cstc.stockregister.constant.EventTypes;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.EventLog;

import java.util.List;

@Slf4j
public class ContractEventCallback implements EventCallback {

    private MQPublisher MQPublisher;
    private String exchangeName;
    private String routingKey;
    private int groupId;
    private String appId;
    //    @Setter
//    private boolean running = false;
    private List<String> eventNameList;

    public ContractEventCallback(MQPublisher mqPublisher, String exchangeName, String routingKey,
                                 int groupId, String appId, List<String> eventNameList) {
        this.MQPublisher = mqPublisher;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.groupId = groupId;
        this.appId = appId;
        this.eventNameList = eventNameList;
    }

    @Override
    public void onReceiveLog(int status, List<EventLog> logs) {
        log.info(
                "ContractEventCallback onPushEventLog" +
                        " status: {}, logs: {}", status, logs);
        // 推送到指定的MQ中
        pushMessage2MQ(groupId, status, logs);
    }

    private void pushMessage2MQ(int groupId,
                                int status, List<EventLog> logs) {
        EventLogPushMessage eventLogPushMessage = new EventLogPushMessage();
        eventLogPushMessage.setEventType(EventTypes.EVENT_LOG_PUSH.getValue());
        eventLogPushMessage.setGroupId(groupId);
        eventLogPushMessage.setStatus(status);
        eventLogPushMessage.setLogs(logs);
        eventLogPushMessage.setAppId(appId);
        MQPublisher.sendToTradeFinishedByString(exchangeName, routingKey,
                eventLogPushMessage.toString());
    }
}
