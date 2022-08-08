package com.cstc.stockregister.event.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "注册合约事件")
public class ReqContractEventRegister {

    /**
     * application which register contract event
     */
    @ApiModelProperty(value = "应用ID",example="be80841b")
    private String appId;

    /**
     * group id
     */
    @ApiModelProperty(value = "区块链组ID",example="1")
    private Integer groupId;

    /**
     * MQ info: exchange name
     */
    @ApiModelProperty(value = "MQ的Exchange名字",example="app002")
    private String exchangeName;

    /**
     * appId as queue name
     */
    @ApiModelProperty(value = "MQ的队列名字",example="queue002")
    private String queueName;

    /**
     * event log push info below
     */
    @ApiModelProperty(value = "监听合约事件的起始块高",example="1")
    private String fromBlock;
    @ApiModelProperty(value = "监听合约事件的终止块高",example="latest")
    private String toBlock;

    /**
     * single contract address
     */
    @ApiModelProperty(value = "监听的合约地址",example="0x31406a7cd73ab05220add308fa8f91b7bcc60913")
    private String contractAddress;

    /**
     * event name list
     */
    @ApiModelProperty(value = "监听的合约事件函数列表",example="[\"LogManageNewAccount(address,address,address)\"]")
    private List<String> topicList;

}
