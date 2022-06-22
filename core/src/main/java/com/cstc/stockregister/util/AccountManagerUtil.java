package com.cstc.stockregister.util;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.ECDSAKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.SM2KeyPair;
import org.fisco.bcos.sdk.crypto.keystore.PEMKeyStore;
import org.springframework.stereotype.Service;

import java.io.InputStream;

public class AccountManagerUtil {

    // cryptoType: 用于需要指定加载的私钥类型
    // hexPrivateKey: 十六进制的私钥字符串
    public CryptoKeyPair loadAccountFromHexPrivateKey(int cryptoType, String hexPrivateKey)
    {
        // 根据cryptoType创建cryptoSuite，cryptoType目前支持：
        // 1. CryptoType.ECDSA_TYPE: 用于创建非国密类型的CryptoSuite
        // 2. CryptoType.SM_TYPE:    用于创建国密类型的CryptoSuite
        CryptoSuite cryptoSuite = new CryptoSuite(cryptoType);
        // 从十六进制私钥字符串hexPrivateKey加载私钥对象
        return cryptoSuite.getKeyPairFactory().createKeyPair(hexPrivateKey);
    }

    /* 示例二：直接加载非国密私钥 ****/
    public CryptoKeyPair loadECDSAAccountFromHexPrivateKey(String hexPrivateKey)
    {
        // 创建国密类型的KeyFactory
        ECDSAKeyPair keyFacotry = new ECDSAKeyPair();
        // 从十六进制字符串加载hexPrivateKey
        return keyFacotry.createKeyPair(hexPrivateKey);
    }

    /* 示例三：直接加载国密私钥 ****/
    public CryptoKeyPair loadGMAccountFromHexPrivateKey(String hexPrivateKey)
    {
        // 创建国密类型的KeyFactory
        SM2KeyPair keyFacotry = new SM2KeyPair();
        // 从十六进制字符串加载hexPrivateKey
        return keyFacotry.createKeyPair(hexPrivateKey);
    }

    // 从pemAccountFilePath指定路径加载pem账户文件，并将其设置为交易发送账户
    public CryptoKeyPair loadPemAccount(Client client, String pemAccountFilePath)
    {
        // 通过client获取CryptoSuite对象
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        // 加载pem账户文件
        cryptoSuite.loadAccount("pem", pemAccountFilePath, null);
        return cryptoSuite.getCryptoKeyPair();
    }

    // 从pemAccountFilePath指定路径加载pem账户文件，并将其设置为交易发送账户
    public CryptoKeyPair loadPemAccount(Client client, InputStream keyStoreFileInputStream)
    {
        // 通过client获取CryptoSuite对象
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        // 加载pem账户文件
        cryptoSuite.createKeyPair(new PEMKeyStore(keyStoreFileInputStream).getKeyPair());
        return cryptoSuite.getCryptoKeyPair();
    }
    public CryptoKeyPair loadP12Account(Client client, String p12AccountFilePath, String password)
    {
        // 通过client获取CryptoSuite对象
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        // 加载pem账户文件
        cryptoSuite.loadAccount("p12", p12AccountFilePath, password);
        return cryptoSuite.getCryptoKeyPair();
    }

    // 将随机生成的账户信息保存在pemFilePath指定的路径
    public void saveAccountWithPem(CryptoKeyPair cryptoKeyPair, String pemFilePath)
    {
        // 以pem的格式保存账户文件到pemFilePath路径
        cryptoKeyPair.storeKeyPairWithPem(pemFilePath);
    }

    // 将随机生成的账户信息保存在账户配置${keyStoreDir}指定的目录下
    public void saveAccountWithPemToKeyStoreDir(CryptoKeyPair cryptoKeyPair)
    {
        // 账户文件名为${accountAddress}.pem
        cryptoKeyPair.storeKeyPairWithPemFormat();
    }

    public void saveAccountWithP12(CryptoKeyPair cryptoKeyPair, String p12FilePath, String password)
    {
        // 以p12的格式将账户文件保存到p12FilePath路径
        cryptoKeyPair.storeKeyPairWithP12(p12FilePath, password);
    }

    // 将随机生成的账户信息保存在账户配置${keyStoreDir}指定的目录下
    public void saveAccountWithP12ToKeyStoreDir(CryptoKeyPair cryptoKeyPair, String password)
    {
        // 账户文件名为${accountAddress}.p12
        cryptoKeyPair.storeKeyPairWithP12Format(password);
    }

}
