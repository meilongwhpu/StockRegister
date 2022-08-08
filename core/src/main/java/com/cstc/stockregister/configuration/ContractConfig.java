package com.cstc.stockregister.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "smart.contract")
public class ContractConfig {

    private String accountManagerAddress;

    private String governorAddress;

    private String deployContractAccount;

    private String groupId;
}
