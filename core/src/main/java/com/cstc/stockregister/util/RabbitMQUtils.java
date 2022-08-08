package com.cstc.stockregister.util;

import com.cstc.stockregister.event.callback.ContractEventCallback;
import org.fisco.bcos.sdk.abi.tools.TopicTools;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.eventsub.EventLogParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RabbitMQUtils {
    public static final String ROUTING_KEY_EVENT = "event";
    /**
     * map of ("registerId", ContractEventCallback instance)
     */
    public static Map<String, ContractEventCallback> CONTRACT_EVENT_CALLBACK_MAP = new ConcurrentHashMap<>();

    /**
     * init EventLogUserParams with single contract address
     * @param fromBlock
     * @param toBlock
     * @param contractAddress
     * @param topicList
     * @return
     */
    public static EventLogParams initSingleEventLogUserParams(String fromBlock, String toBlock,
                                                              String contractAddress, List<String> topicList, CryptoSuite cryptoSuite) {
        EventLogParams params = new EventLogParams();
        params.setFromBlock(fromBlock);
        params.setToBlock(toBlock);

        // addresses，设置为Java合约对象的地址
        List<String> addresses = new ArrayList<>();
        if(contractAddress!=null&&!"".equals(contractAddress.trim())){
            addresses.add(contractAddress);
        }
        params.setAddresses(addresses);

        List<Object> topics = new ArrayList<>();
        // put multiple event in topics[0]
        List<String> topicSigList = new ArrayList<>();
        TopicTools tool = new TopicTools(cryptoSuite);
        topicList.forEach(t -> topicSigList.add(tool.stringToTopic(t)));
        topics.add(topicSigList);
        params.setTopics(topics);

        return params;
    }


}
