pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Organization.sol";
import "./govAccount/AccountManager.sol";
import "./storage/AddressSetLib.sol";
import "./storage/Bytes2AddressMapLib.sol";
import "./AccountData.sol";
import "./StockAsset.sol";
import "./Constant.sol";

contract Governor is Constant {
    using AddressSetLib for AddressSetLib.Set;
    using Bytes2AddressMapLib for Bytes2AddressMapLib.Map;

    address owner;
    address accountManager;

    //公司列表
    Bytes2AddressMapLib.Map organizationList;

    //股权资产列表
    Bytes2AddressMapLib.Map stockAssetList;

    modifier onlyOwner() {
        require(msg.sender == owner && tx.origin==owner, "only owner is authorized.");
        _;
    }


    constructor (address accountManagerAddress) public{
        owner = msg.sender;
        accountManager = accountManagerAddress;
    }

    function getOwner() public view returns (address) {
        return owner;
    }

    function getAccountManager() public view returns (address){
        return accountManager;
    }

    function getOrgAddress(bytes orgCreditCode)public view returns(address organization){
        organization=organizationList.get(orgCreditCode);
    }

    function getStockAssetAddress(bytes assetCode)public view returns(address stockAsset){
        stockAsset=stockAssetList.get(assetCode);
    }

    function createAccount(address externalAccount) public onlyOwner returns(bool,address){
        AccountData data = new AccountData(tx.origin,externalAccount);
        bool result;
        address _userAccount;
        (result,_userAccount)=AccountManager(accountManager).newAccount(externalAccount, data);
        require(EMPTY_ADDR!=_userAccount,"create UserAccount failed");
        return (result,_userAccount);
    }

    function createOrganization(bytes[] orgInfoStr) public onlyOwner returns (bool,address){
        Organization organization = new Organization(tx.origin,accountManager,orgInfoStr);
        require(EMPTY_ADDR!=address(organization),"create organization failed");
        organizationList.add(orgInfoStr[1],organization);
        return (true,address(organization));
    }
    //先更新Creator再更新Governor
    function updateGovernorOfOrganization(bytes orgCreditCode,address newGovernor)public onlyOwner returns(address oldGovernor){
        address orgAddress=organizationList.get(orgCreditCode);
        require(EMPTY_ADDR!=orgAddress,"find organization failed");
        Organization organization=Organization(orgAddress);
        oldGovernor=organization.getGovernor();
        organization.setGovernor(newGovernor);
    }
    //先更新Creator再更新Governor
    function updateCreatorOfOrganization(bytes orgCreditCode,address newCreator)public onlyOwner returns(address oldCreator){
        address orgAddress=organizationList.get(orgCreditCode);
        require(EMPTY_ADDR!=orgAddress,"find organization failed");
        Organization organization=Organization(orgAddress);
        oldCreator=organization.getCreator();
        //因为Organization的Creator和owner相同，所以同时更新
        organization.setCreator(newCreator);
        organization.setOwner(newCreator);
    }


    //inDataStr[0]=assetCode,inDataStr[1]=assetName,
    //inDataUint[0]=totalBalances,inDataUint[1]=_rSalesRatio,inDataUint[2]=tradeRestrictDay
    function createStockAsset(bytes orgCreditCode, bytes[] inDataStr,uint256[] inDataUint) public onlyOwner returns(bool,address){
        address orgPublisher=organizationList.get(orgCreditCode);
        require(EMPTY_ADDR!=orgPublisher,"find Organization failed");
        address[] memory relatedAddress=new address[](3);
        relatedAddress[0]=tx.origin;
        relatedAddress[1]=orgPublisher;
        relatedAddress[2]=accountManager;
        StockAsset stockAsset=new StockAsset(relatedAddress,inDataStr,inDataUint);
        require(EMPTY_ADDR!=address(stockAsset),"create StockAsset failed");
        stockAssetList.add(inDataStr[0],address(stockAsset));
        return (true, address(stockAsset));
    }

    //先更新Creator再更新Governor
    function updateGovernorOfStockAsset(bytes assetCode,address newGovernor)public onlyOwner returns(address oldGovernor){
        address stockAssetAddress=stockAssetList.get(assetCode);
        require(EMPTY_ADDR!=stockAssetAddress,"find stockAsset failed");
        StockAsset stockAsset=StockAsset(stockAssetAddress);
        oldGovernor=stockAsset.getGovernor();
        stockAsset.setGovernor(newGovernor);
    }

    //先更新Creator再更新Governor
    function updateCreatorOfStockAsset(bytes assetCode,address newCreator)public  onlyOwner returns(address oldCreator){
        address stockAssetAddress=stockAssetList.get(assetCode);
        require(EMPTY_ADDR!=stockAssetAddress,"find stockAsset failed");
        StockAsset stockAsset=StockAsset(stockAssetAddress);
        oldCreator=stockAsset.getCreator();
        //因为Organization的Creator和owner相同，所以同时更新
        stockAsset.setCreator(newCreator);
        stockAsset.setOwner(newCreator);
    }


    //更新Organization合约的状态：0-正常，1-停止
    function updateOrgContractStatus(bytes orgCreditCode,uint8 newStatus)public onlyOwner returns(bool result){
        address orgAddress=organizationList.get(orgCreditCode);
        require(EMPTY_ADDR!=orgAddress,"find organization failed");
        Organization organization=Organization(orgAddress);
        if(newStatus==0){
            result=organization.restartContract();
        }else{
            result=organization.stopContract();
        }
    }

    //更新Organization合约的状态：0-正常，1-停止
    function updateStockContractStatus(bytes assetCode,uint8 newStatus)public onlyOwner returns(bool result){
        address stockAssetAddress=stockAssetList.get(assetCode);
        require(EMPTY_ADDR!=stockAssetAddress,"find stockAsset failed");
        StockAsset stockAsset=StockAsset(stockAssetAddress);
        if(newStatus==0){
            result=stockAsset.restartContract();
        }else{
            result=stockAsset.stopContract();
        }
    }

}