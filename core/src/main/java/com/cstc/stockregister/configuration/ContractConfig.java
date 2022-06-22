package com.cstc.stockregister.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ContractConfig {

    @Value("${smart.contract.accountManager.address}")
    private String accountManagerAddress;

    @Value("${smart.contract.governor.address}")
    private String governorAddress;

    @Value("${deploy.contract.account}")
    private String deployContractAccount;

    @Value("${fisco.bcos.group.id}")
    private int groupId;

    @Value("${account.pemFile.path}")
    private String pemFilePath;

    public String getAccountManagerAddress() {
        return accountManagerAddress;
    }

    public void setAccountManagerAddress(String accountManagerAddress) {
        this.accountManagerAddress = accountManagerAddress;
    }

    public String getGovernorAddress() {
        return governorAddress;
    }

    public void setGovernorAddress(String governorAddress) {
        this.governorAddress = governorAddress;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getDeployContractAccount() {
        return deployContractAccount;
    }

    public void setDeployContractAccount(String deployContractAccount) {
        this.deployContractAccount = deployContractAccount;
    }
}
