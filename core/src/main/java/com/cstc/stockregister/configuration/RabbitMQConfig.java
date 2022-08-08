package com.cstc.stockregister.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {

    /**
     * 新建yml中rabbitmq-queue默认的队列
     * @param connectionFactory spring的yml中rabbitmq项配置
     * @return
     */
    @Bean(name = "rabbitAdmin")
    public RabbitAdmin initRabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        return rabbitAdmin;
    }

    /**
     * 用于发送消息到队列
     * 传输的message消息体在Message实体类中使用jackson进行序列化
     * @param rabbitAdmin
     * @return
     */
    @Bean(name = "rabbitTemplate")
    public RabbitTemplate getRabbitTemplate(RabbitAdmin rabbitAdmin) {
        RabbitTemplate rabbitTemplate = rabbitAdmin.getRabbitTemplate();
        return rabbitTemplate;
    }

}
