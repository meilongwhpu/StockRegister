package com.cstc.stockregister.rpc.service;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import java.math.BigInteger;
import java.util.List;

@JsonRpcService(value="rpc/governor")
public interface GovernorRpcService {

    public String createAccount(@JsonRpcParam(value = "externalAccount") String externalAccount);

    //orgInfoStr: 0-orgName;1-orgCreditCode;2-legalPerson;3-legalIdNumber;4-registeredAddress;5-officeAddress;6-contactNumber;
    //7-credType;8-orgType;9-registryDate;10-registeredCapital;11-paidInCapital;12-shareholdersNumber;13-legalIdType;
    public String createOrganization(@JsonRpcParam(value = "orgInfoStr")List<String> orgInfoStr);

    //inDataStr[0]=assetCode,inDataStr[1]=assetName
    //inDataUint[0]=totalBalances,inDataUint[1]=_rSalesRatio,inDataUint[2]=tradeRestrictDay
    public String createStockAsset(@JsonRpcParam(value = "orgCreditCode")String orgCreditCode, @JsonRpcParam(value = "inDataStr")List<String> inDataStr, @JsonRpcParam(value = "inDataUint")List<BigInteger> inDataUint);

}
