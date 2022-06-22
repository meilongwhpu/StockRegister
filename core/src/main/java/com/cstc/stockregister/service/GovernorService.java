package com.cstc.stockregister.service;

import java.math.BigInteger;
import java.util.List;

public interface GovernorService {

    public String createAccount(String externalAccount)throws Exception;

    public String createOrganization(List<String> orgInfoStr)throws Exception;

    public String createStockAsset(String orgCreditCode, List<String> inDataStr, List<BigInteger> inDataUint)throws Exception;

    public String getOrgAddress(String orgCreditCode)throws Exception;

    public String getStockAssetAddress(String assetCode)throws Exception;

    public String updateGovernorOfOrganization(String orgCreditCode,String newGovernor)throws Exception;
    public String updateCreatorOfOrganization(String orgCreditCode,String newCreator)throws Exception;

    public String updateGovernorOfStockAsset(String assetCode,String newGovernor)throws Exception;
    public String updateCreatorOfStockAsset(String assetCode,String newCreator)throws Exception;

    public boolean updateOrgContractStatus(String orgCreditCode,int newStatus)throws Exception;
    public boolean updateStockContractStatus(String assetCode,int newStatus)throws Exception;

}
