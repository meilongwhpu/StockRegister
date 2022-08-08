package com.cstc.stockregister.service.impl;

import com.cstc.stockregister.contracts.Governor;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderInterface;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class GenerateContractService extends  BaseService{
    @Bean
    public Governor getGovernor(){
        return this.getContract(contractConfig.getGovernorAddress(),Governor.class);
    }

    @Bean(name="decoder")
    public TransactionDecoderInterface getDecoder(){
        return new TransactionDecoderService(this.getClient().getCryptoSuite());
    }
}
