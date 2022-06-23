package com.cstc.stockregister.service.impl;

import com.cstc.stockregister.configuration.BcosConfig;
import com.cstc.stockregister.configuration.ContractConfig;
import com.cstc.stockregister.constant.SysConstant;
import com.cstc.stockregister.util.AccountManagerUtil;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

@Slf4j
public class BaseService {

    private AccountManagerUtil accountManagerUtil=new AccountManagerUtil();

    private final BcosConfig bcosConfig;

    private final ContractConfig contractConfig;

    private static BcosSDK bcosSDK;
    private static Client client;
    private CryptoKeyPair cryptoKeyPair;


    public BaseService(BcosConfig bcosConfig,ContractConfig contractConfig) {
        this.bcosConfig=bcosConfig;
        this.contractConfig=contractConfig;
        ConfigProperty property = new ConfigProperty();
        Map peers = bcosConfig.getNetwork();
        property.setNetwork(peers);
        Map<String, Object> cryptoMaterials = bcosConfig.getCryptoMaterial();
        property.setCryptoMaterial(cryptoMaterials);
        ConfigOption configOption = null;
        try {
            configOption = new ConfigOption(property);
        } catch (ConfigException e) {
            e.printStackTrace();
            log.error("初始化BaseService对象失败",e);
        }
        synchronized(BaseService.class) {
            if (bcosSDK==null && client == null) {
                bcosSDK=new BcosSDK(configOption);
                client = bcosSDK.getClient(contractConfig.getGroupId());
            }
        }
    }


    public void initialize(String accountAddress) throws Exception {
        if(SysConstant.DEFAULT_GROUP_ID!=Integer.valueOf(contractConfig.getGroupId())){
            client = bcosSDK.getClient(Integer.valueOf(contractConfig.getGroupId()));
        }
        Resource res=loadClassPathFileAsSpringResource("classpath:accounts/"+accountAddress+".pem");
        cryptoKeyPair=accountManagerUtil.loadPemAccount(client,res.getInputStream());
        //String pemAccountFilePath= contractConfig.getPemFilePath()+ File.separator+accountAddress+".pem";
        //cryptoKeyPair=accountManagerUtil.loadPemAccount(client,pemAccountFilePath);
    }

    public static Resource loadClassPathFileAsSpringResource(String classpathFilePath) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        return resourceLoader.getResource(classpathFilePath);
    }

    public Client getClient() {
        return client;
    }

    public CryptoKeyPair getCryptoKeyPair() {
        return cryptoKeyPair;
    }

    public <T> T getContract(String contractAddress, Object... params) {
        Class type = (Class) params[0];
        T contract = null;
        try {
            Method loader = type.getMethod("load",
                    String.class,
                    Client.class,
                    CryptoKeyPair.class);
            contract = (T) loader.invoke(
                    null,
                    contractAddress, client, cryptoKeyPair);
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
            CryptoKeyPair cryptoKeyPair=accountManagerUtil.loadPemAccount(client,pemAccountFilePath);
            Method loader = type.getMethod("load",
                    String.class,
                    Client.class,
                    CryptoKeyPair.class);
            contract = (T) loader.invoke(
                    null,
                    contractAddress, client, cryptoKeyPair);
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
