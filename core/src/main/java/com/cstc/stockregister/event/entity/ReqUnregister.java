package com.cstc.stockregister.event.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ReqUnregister {
    /**
     * id in database
     */
    private String id;

    /**
     * application which register new block event
     */
    @NotEmpty(message = "appId cannot be empty")
    private String appId;

    /**
     * group id
     */
    @NotNull(message = "groupId cannot be empty")
    private Integer groupId;

    /**
     * MQ info: exchange name
     */
    @NotEmpty(message = "exchangeName cannot be empty")
    private String exchangeName;

    /**
     * username as queue name
     */
    @NotEmpty(message = "queueName cannot be empty, usually use username")
    private String queueName;
}
