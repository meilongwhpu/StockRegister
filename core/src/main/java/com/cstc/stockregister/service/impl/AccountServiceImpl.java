package com.cstc.stockregister.service.impl;

import com.cstc.stockregister.configuration.BcosConfig;
import com.cstc.stockregister.configuration.ContractConfig;
import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.contracts.AccountData;
import com.cstc.stockregister.contracts.gov_account.AccountManager;
import com.cstc.stockregister.contracts.gov_account.UserAccount;
import com.cstc.stockregister.exception.ContractBaseException;
import com.cstc.stockregister.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl extends BaseService implements AccountService {

    private AccountManager accountManager;

    public AccountServiceImpl(BcosConfig bcosConfig, ContractConfig contractConfig) {
        super(bcosConfig,contractConfig);
        try {
            this.initialize(contractConfig.getDeployContractAccount());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化AccountService对象失败",e);
        }
        accountManager=this.getContract(contractConfig.getAccountManagerAddress(), AccountManager.class);

    }

    @Override
    public String getUserAccount(String externalAccount) throws Exception {
        return accountManager.getUserAccount(externalAccount);
    }

    @Override
    public String getExternalAccount(String interAccount)throws Exception {
        return accountManager.getExternalAccount(interAccount);
    }

    @Override
    public boolean hasAccount(String externalAccount) throws Exception {
        return accountManager.hasAccount(externalAccount);
    }

    @Override
    public boolean setExternalAccountByGovernance(String newExternalAccount, String oldExternalAccount) throws Exception {
        TransactionReceipt receipt=accountManager.setExternalAccountByGovernance(newExternalAccount,oldExternalAccount);
        Tuple1<Boolean> result=accountManager.getSetExternalAccountByGovernanceOutput(receipt);
        log.info("调用AccountManager合约的setExternalAccountByGovernance方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        return result.getValue1();
    }

    @Override
    public boolean setExternalAccountByUser(String senderAddress,String newExternalAccount) throws Exception {
        AccountManager accountManager=this.getContractBySender(senderAddress,this.getContractConfig().getAccountManagerAddress(),AccountManager.class);
        TransactionReceipt receipt=accountManager.setExternalAccountByUser(newExternalAccount);
        Tuple1<Boolean> result=accountManager.getSetExternalAccountByUserOutput(receipt);
        log.info("调用AccountManager合约的setExternalAccountByUser方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        return result.getValue1();
    }

    @Override
    public boolean addStockAsset(String externalAccount, String stockAssetCode, String assetAddress) throws Exception {
        AccountData accountData=loadAccountData(externalAccount);
        TransactionReceipt receipt=accountData.addStockAsset(stockAssetCode.getBytes(),assetAddress);
        log.info("调用AccountManager合约的addStockAsset方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        return accountData.getAddBondAssetOutput(receipt).getValue1();
    }

    @Override
    public String getStockAssets(String externalAccount, String stockAssetCode) throws Exception {
        AccountData accountData=loadAccountData(externalAccount);
        try{
            String result=accountData.getStockAssets(stockAssetCode.getBytes());
            return result;
        }catch (ContractException e){
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),e.getMessage());
        }

    }

    private AccountData loadAccountData(String externalAccount)throws Exception {
        String userAccountAddress=accountManager.getUserAccount(externalAccount);
        UserAccount userAccountContract=this.getContractBySender(externalAccount,userAccountAddress,UserAccount.class);
        TransactionReceipt receipt=userAccountContract.getData();
        Tuple1<String> accountDataAddress=userAccountContract.getGetDataOutput(receipt);
        AccountData accountData=this.getContractBySender(externalAccount,accountDataAddress.getValue1(), AccountData.class);
        return accountData;
    }
}
