pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./lib/UtilLib.sol";
import "./lib/LibTypeConversion.sol";
import "./lib/LibSafeMath.sol";
import "./Identity.sol";
import "./storage/Address2BytesMapLib.sol";
import "./storage/AddressSetLib.sol";
import "./storage/Bytes2AddressMapLib.sol";
import "./govAccount/AccountManager.sol";

contract Organization is Identity {
    using Address2BytesMapLib for Address2BytesMapLib.Map;
    using AddressSetLib for AddressSetLib.Set;
    using Bytes2AddressMapLib for Bytes2AddressMapLib.Map;
    using UtilLib for *;
    using LibSafeMath for uint256;
    using LibTypeConversion for *;

    address accountManager;

    //企业管理员
    Address2BytesMapLib.Map admins;
    //股权资产合约地址
    address stockAsset;
    //发行的债权资产合约地址列表
    Bytes2AddressMapLib.Map bondAssetMap;

    //0-orgName;1-orgCreditCode;2-legalPerson;3-legalIdNumber;4-registeredAddress;5-registryDate;6-officeAddress;7-contactNumber;
    //8-credType;9-orgType;10-registeredCapital;11-paidInCapital;12-shareholdersNumber;13-legalIdType;
    bytes[] attrValues;
    //董监高的账户地址
    AddressSetLib.Set managerList;

    uint256  currentNumber;

    uint lastBlock;
    event UpdateOrgInfoRecord(uint number,bytes[] orgInfoStr,uint previousBlock);

    modifier isExistAccount(address _externalAccount) {
        require(AccountManager(accountManager).hasAccount(_externalAccount), "externalAccount is not open an account.");
        _;
    }

    constructor(address _owner,address _accountManager,bytes[] orgInfoStr)  Identity(msg.sender,_owner,_owner)  public {
        accountManager=_accountManager;
        for(uint i=0;i<orgInfoStr.length;i++){
            attrValues.push(orgInfoStr[i]);
        }
        currentNumber=currentNumber.add(1);
        emit UpdateOrgInfoRecord(currentNumber,orgInfoStr,lastBlock);
        lastBlock=block.number;
    }

    function updateOrgInfo(bytes[] orgInfoStr) public onlyCreator  returns(uint){
        uint updateLength=orgInfoStr.length<=attrValues.length?orgInfoStr.length:attrValues.length;
        uint i=0;
        while(i<updateLength){
            attrValues[i]=orgInfoStr[i];
            i++;
        }
        if(orgInfoStr.length>attrValues.length){
            while(i<orgInfoStr.length){
                attrValues.push(orgInfoStr[i]);
                i++;
            }
        }
        currentNumber=currentNumber.add(1);
        emit UpdateOrgInfoRecord(currentNumber,orgInfoStr,lastBlock);
        lastBlock=block.number;
        return currentNumber;
    }

    function getOrgInfo() public view returns(bytes[],address){
        return (attrValues,stockAsset);
    }

    function getLastBlock()public view returns(uint){
        return lastBlock;
    }

    function addManager(address[] managers) public onlyCreator  returns(uint){
        AccountManager accountManagerCont=AccountManager(accountManager);
        for(uint i=0;i<managers.length;i++){
            require(accountManagerCont.hasAccount(managers[i]), "managerAddress is not open an account.");
            managerList.insert(accountManagerCont.getUserAccount(managers[i]));
        }
        return managerList.size();
    }

    function deleteManager(address managerAddress) public isExistAccount(managerAddress) onlyCreator returns(bool){
        address account=AccountManager(accountManager).getUserAccount(managerAddress);
        return managerList.remove(account);
    }

    function getManager()public view returns(address[]){
        return managerList.getAll();
    }

    function setStockAsset(address _stockAsset) public onlyCreator  returns(bool){
        stockAsset=_stockAsset;
        return true;
    }

    function setBondAsset(bytes bondAssetCode,address bondAssetAddress) public onlyCreator  returns(bool){
        return bondAssetMap.add(bondAssetCode,bondAssetAddress);
    }

    function getBondAsset(bytes bondAssetCode) public onlyCreator view returns(address){
        return bondAssetMap.get(bondAssetCode);
    }

    function setOrgAdmin(address adminAccount,bytes desc) public onlyCreator isExistAccount(adminAccount) returns(bool){
        address account=AccountManager(accountManager).getUserAccount(adminAccount);
        return admins.add(account,desc);
    }

    function removeOrgAdmin(address adminAccount) public onlyCreator isExistAccount(adminAccount) returns(bool){
        address account=AccountManager(accountManager).getUserAccount(adminAccount);
        return admins.remove(account);
    }
}