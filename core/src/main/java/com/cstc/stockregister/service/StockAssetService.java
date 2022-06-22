package com.cstc.stockregister.service;

import com.cstc.stockregister.entity.*;

import java.math.BigInteger;
import java.util.List;

public interface StockAssetService {

    public boolean updateTotalSupply(String assetCode,BigInteger _totalBalances)throws Exception;

    public BigInteger totalSupply(String assetCode)throws Exception;

    public AccountBookDTO balanceByAccount(String assetCode,String _externalAccount)throws Exception;

    public List<TransactionDTO> getTransRestrictedDetailByAccount(String assetCode,String _externalAccount)throws Exception;

    public PledgeDTO getPledgeDetailByNumber(String assetCode,String _externalAccount, BigInteger _pledgeNumber)throws Exception;

    public List<PledgeDTO> getPledgeDetailByAccount(String assetCode,String _externalAccount)throws Exception;

    public FrozenDTO getFrozenDetailByNumber(String assetCode,String _externalAccount, BigInteger _frozenNumber)throws Exception;

    public List<FrozenDTO> getFrozenDetailByAccount(String assetCode,String _externalAccount) throws Exception;

    public int getNextWaitingFrozenNumber(String assetCode,String _externaltoAccount, BigInteger unFrozenNumber)throws Exception;

    public int transferByAdmin(String assetCode,String _externalFromAccount, String _externaltoAccount, BigInteger amount, BigInteger transferType)throws Exception;

    public int cancelTransRestrictions(String assetCode,String _externalAccount, List<BigInteger> _transNumber)throws Exception;

    public int incrBalance(String assetCode,String _externalAccount, BigInteger amount, int isRSales)throws Exception;

    public int reduceBalance(String assetCode,String _externalAccount, BigInteger amount)throws Exception;

    public int doPledge(String assetCode,List<BigInteger> balanceData, List<String> pledgeDetail, String causeDesc)throws Exception;

    public boolean undoPledge(String assetCode,String pledgor, List<BigInteger> balanceData, BigInteger _pledgeNumber)throws Exception;

    public int doFrozen(String assetCode,List<BigInteger> balanceData, List<String> frozenDetail)throws Exception;

    public boolean doWaitingFrozen(String assetCode, List<BigInteger> balanceData, String ownerAddress, int waitingFrozenSerilNum, int requestAmount)throws Exception;

    public int undoFrozen(String assetCode,String assetOwnerAddress, List<BigInteger> balanceData, BigInteger _frozenNumber)throws Exception;

    public StockBusinessRecordDTO getBusinessRecord(AccountService accountService,String assetCode, String externalAccount,int businessType)throws Exception;
    //获取股权合约所属的治理合约地址
    public String getGovernor(String assetCode)throws Exception;
    //获取股权合约的创建者地址
    public String getCreator(String assetCode)throws Exception;
}
