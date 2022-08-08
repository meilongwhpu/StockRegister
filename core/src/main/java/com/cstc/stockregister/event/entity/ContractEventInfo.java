package com.cstc.stockregister.event.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_contract_event", columnNames = {"appId", "exchangeName",
                "queueName", "contractAddress"})
})
public class ContractEventInfo {

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    private String id;

    /**
     * event type: 1: newBlockEvent, 2: contractEvent, 3: others
     */
    private Integer eventType;

    /**
     * application id to register
     */
    private String appId;

    /**
     * group id
     */
    private Integer groupId;

    /**
     * MQ info
     */
    private String exchangeName;

    /**
     * @appId as queue name
     */
    private String queueName;

    /**
     * concat appId + "_" + event/block + "_" + randomStr as routing key
     */
    private String routingKey;

    /**
     * if use DecodedEventLogPushCallback, needs abi for decoder
     */
    @Column(columnDefinition = "mediumtext")
    private String contractAbi;

    /**
     * EventLogUserParams info
     */
    private String fromBlock;
    private String toBlock;
    /**
     * single contract address
     */
    private String contractAddress;
    /**
     * List<String> event name list
     */
    @Column(columnDefinition = "text")
    private String topicList;

    /**
     * registerId when register callback in groupManagerService
     */
    private String registerId;

    private LocalDateTime createTime;
}
