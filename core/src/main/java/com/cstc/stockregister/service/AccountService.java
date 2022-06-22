package com.cstc.stockregister.service;

public interface AccountService {

    public String getUserAccount(String externalAccount)throws Exception;
    public String getExternalAccount(String interAccount)throws Exception;
    public boolean hasAccount(String externalAccount)throws Exception;
    public boolean setExternalAccountByGovernance(String newExternalAccount, String oldExternalAccount)throws Exception;
    public boolean setExternalAccountByUser(String senderAddress,String newExternalAccount)throws Exception;
    public boolean addStockAsset(String newExternalAccount, String stockAssetCode, String assetAddress)throws Exception;
    public String getStockAssets(String newExternalAccount, String stockAssetCode) throws Exception;
}
