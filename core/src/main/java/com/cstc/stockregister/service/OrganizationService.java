package com.cstc.stockregister.service;

import com.cstc.stockregister.entity.ManagerAddressDTO;
import com.cstc.stockregister.entity.OrgInfoChangeDTO;
import com.cstc.stockregister.entity.OrganizationDTO;
import com.cstc.stockregister.entity.StockBusinessRecordDTO;

import java.math.BigInteger;
import java.util.List;

public interface OrganizationService {
    //更新企业信息
    public int updateOrgInfo(String orgCreditCode,List<String> orgInfoStr) throws Exception;

    //获取企业信息
    public OrganizationDTO getOrgInfo(String orgCreditCode) throws Exception;

    //设置企业发行的股权合约地址
    public boolean setStockAsset(String orgCreditCode,String stockAssetAddress)throws Exception;

    //设置企业发行的债权合约地址
    public boolean setBondAsset(String orgCreditCode,String bondAssetCode, String bondAssetAddress)throws Exception;

    //根据债权代码获取债权合约地址
    public String getBondAsset(String orgCreditCode,String bondAssetCode)throws Exception;

    //添加董监高
    public int addManager(String orgCreditCode,List<String> managers)throws Exception;
    //删除董监高
    public boolean deleteManager(String orgCreditCode,String managerAddress)throws Exception;
    //查询企业的董监高名单
    public List<String> getManager(String orgCreditCode)throws Exception;
    //获取更新企业资料的记录
    public List<OrgInfoChangeDTO> getOrgInfoChangeRecord(String orgCreditCode) throws Exception;

    //获取企业合约所属的治理合约地址
    public String getGovernor(String orgCreditCode)throws Exception;
    //获取企业合约的创建者地址
    public String getCreator(String orgCreditCode)throws Exception;

}
