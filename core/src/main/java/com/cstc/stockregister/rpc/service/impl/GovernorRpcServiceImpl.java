package com.cstc.stockregister.rpc.service.impl;

import com.cstc.stockregister.configuration.ContractConfig;
import com.cstc.stockregister.rpc.service.GovernorRpcService;
import com.cstc.stockregister.service.GovernorService;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@AutoJsonRpcServiceImpl
@Service
@Slf4j
public class GovernorRpcServiceImpl implements GovernorRpcService {

    @Autowired
    private GovernorService governorService;


    @Override
    public String createAccount(String externalAccount) {
        try {
            String responseContract=governorService.createAccount(externalAccount);
            return responseContract;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String createOrganization(List<String> orgInfoStr) {
        try {
            String responseContract=governorService.createOrganization(orgInfoStr);
            return responseContract;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String createStockAsset(String orgCreditCode, List<String> inDataStr, List<BigInteger> inDataUint) {
        try {
            String responseContract=governorService.createStockAsset(orgCreditCode,inDataStr,inDataUint);
            return responseContract;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }
}
