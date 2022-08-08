package com.cstc.stockregister.service.impl;

import com.cstc.stockregister.configuration.BcosConfig;
import com.cstc.stockregister.configuration.ContractConfig;
import com.cstc.stockregister.contracts.Organization;
import com.cstc.stockregister.contracts.StockAsset;
import com.cstc.stockregister.util.AccountManagerUtil;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.EventEncoder;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;

@Slf4j
@Service
@EnableConfigurationProperties({BcosConfig.class,ContractConfig.class})
public class BaseService {

    @Autowired
    private AccountManagerUtil accountManagerUtil;

    @Autowired
    private  BcosConfig bcosConfig;
    @Autowired
    public ContractConfig contractConfig;

    @Autowired
    protected BcosSDK bcosSDK;

    private Client client;

    private CryptoKeyPair cryptoKeyPair;

    /**
     * The topic map.
     */
    private HashMap<String, String> organizationTopicMap;

    private HashMap<String, String> stockTopicMap;

    public HashMap<String, String> getStockTopicMap(){
        if(stockTopicMap==null||stockTopicMap.size()==0) {
            stockTopicMap = new HashMap<String, String>();
            stockTopicMap.put(new EventEncoder(this.getClient().getCryptoSuite()).encode(StockAsset.STOCKBUSINESSRECORD_EVENT), StockAsset.STOCKBUSINESSRECORD_EVENT.getName());
        }
        return stockTopicMap;
    }

    public HashMap<String, String> getOrganizationTopicMap(){
        if(organizationTopicMap==null||organizationTopicMap.size()==0){
            organizationTopicMap = new HashMap<String, String>();
            organizationTopicMap.put(new EventEncoder(this.getClient().getCryptoSuite()).encode(Organization.UPDATEORGINFORECORD_EVENT),Organization.UPDATEORGINFORECORD_EVENT.getName());
        }
        return organizationTopicMap;
    }

    public static Resource loadClassPathFileAsSpringResource(String classpathFilePath) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        return resourceLoader.getResource(classpathFilePath);
    }

    public Client getClient() {
        if(client==null){
            client=bcosSDK.getClient(Integer.valueOf(contractConfig.getGroupId()));
        }
        return client;
    }


    public CryptoSuite getCryptoSuite() {
        int encryptType = bcosSDK.getGroupManagerService().getCryptoType(bcosConfig.getNetwork().get("peers").get(0));
        log.info("getCommonSuite init encrypt type:{}", encryptType);
        return new CryptoSuite(encryptType);
    }

    public <T> T getContract(String contractAddress, Object... params) {
        Resource res=loadClassPathFileAsSpringResource("classpath:accounts/"+contractConfig.getDeployContractAccount()+".pem");
        try {
            cryptoKeyPair=accountManagerUtil.loadPemAccount(this.getClient(),res.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("初始化BaseService对象失败",e);
        }
        Class type = (Class) params[0];
        T contract = null;
        try {
            Method loader = type.getMethod("load",
                    String.class,
                    Client.class,
                    CryptoKeyPair.class);
            contract = (T) loader.invoke(
                    null,
                    contractAddress, this.getClient(), cryptoKeyPair);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导入合约失败",e);
        }
        return contract;
    }

    public <T> T getContractBySender(String contractAddress, Object... params) {
        String sender=(String)params[0];
        Class type = (Class) params[1];
        T contract = null;
        try {
            URL url=Thread.currentThread().getContextClassLoader().getResource("accounts");
            String pemAccountFilePath= url.getPath()+ File.separator+sender+".pem";
            CryptoKeyPair cryptoKeyPair=accountManagerUtil.loadPemAccount(this.getClient(),pemAccountFilePath);
            Method loader = type.getMethod("load",
                    String.class,
                    Client.class,
                    CryptoKeyPair.class);
            contract = (T) loader.invoke(
                    null,
                    contractAddress, this.getClient(), cryptoKeyPair);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return contract;
    }

    public ContractConfig getContractConfig() {
        return contractConfig;
    }



}
