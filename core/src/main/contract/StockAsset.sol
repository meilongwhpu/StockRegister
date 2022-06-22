pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Identity.sol";
import "./AssetData.sol";
import "./lib/LibSafeMath.sol";
import "./govAccount/AccountManager.sol";
import "./lib/LibTypeConversion.sol";
import "./lib/UtilLib.sol";
import "./Constant.sol";
import "./lib/DateTimeUtil.sol";

contract StockAsset is Identity,Constant {

    using LibSafeMath for uint256;
    using LibTypeConversion for *;
    using UtilLib for *;
    address accountManager;
    //股票发行人
    address orgPublisher;
    //资产存放的数据合约地址
    address assetData;
    // 股票价格
    uint256 price;
    // 限售比例
    uint256 rSalesRatio;
    //交易限售天数
    uint tradeRestrictDay;
    DateTimeUtil dateTimeUtil;

    //股权业务事件
    //businessType:1-增资,2-减资,3-交易过户转入，4-交易过户转出，5-非交易过户转入，6-非交易过户转出，7-质押，8-解押，9-司法冻结，10-手工冻结,11--轮候冻结 ,12-解冻
    //content[0]=operator,content[1]=from,content[2]=to,content[3]=amount(业务发生额),content[4]=doFrozenamount(冻结执行金额),content[5]=waitingFrozenamount(等待冻结金额),content[6]=serilNum,
    //content[7]=startTime,content[8]=endTime,content[9]=cause,content[10]=frozenStatus
    event StockBusinessRecord(uint indexed businessType,string account,string[] content,uint previousBlock);

    modifier isExistAccount(address _externalAccount) {
        require(tx.origin == owner,"only owner is authorized.");
        require(AccountManager(accountManager).hasAccount(_externalAccount), "externalAccount is not exist.");
        _;
    }
    modifier checkContractStatus() {
        require(_contractStatus == 0, "only contract status is normal.");
        _;
    }

    //relatedAddress[0]=_creator,relatedAddress[1]=_orgPublisher,relatedAddress[2]=_accountManager
    //inDataStr[0]=assetCode,inDataStr[1]=assetName,
    //inDataUint[0]=totalBalances,inDataUint[1]=_rSalesRatio,inDataUint[2]=tradeRestrictDay
    constructor(address[] relatedAddress,bytes[] inDataStr,uint256[] inDataUint)  Identity(msg.sender, relatedAddress[0],relatedAddress[0]) public{
        require(relatedAddress.length==3,"the length of relatedAddress array is 3");
        require(inDataStr.length==2,"the length of inDataStr array is 2");
        require(inDataUint.length==3,"the length of inDataUint array is 3");
        require(inDataUint[1]>=0&&inDataUint[1]<=100,"rSalesRatio between 0 and 100");
        require(inDataUint[0]>0,"totalBalances greater than 0");
        AssetData asset=new AssetData(relatedAddress[0],msg.sender,inDataStr,inDataUint[0]);

        assetData=address(asset);
        orgPublisher=relatedAddress[1];
        rSalesRatio=inDataUint[1];
        tradeRestrictDay=inDataUint[2];
        accountManager=relatedAddress[2];
        dateTimeUtil=new DateTimeUtil();
    }

    function setPrice(uint256 priceVal) public onlyCreator {
        price = priceVal;
    }

    function setSalesRatio(uint256 _rSalesRatio) public onlyCreator{
        rSalesRatio = _rSalesRatio;
    }

    function setTradeRestrictDay(uint256 _tradeRestrictDay) public onlyCreator{
        tradeRestrictDay = _tradeRestrictDay;
    }

    function getStockBaseInfo()public view returns(uint256,uint256,uint256){
        return (price,rSalesRatio,tradeRestrictDay);
    }

    function totalSupply() public view returns (uint256){
        AssetData asset=AssetData(assetData);
        return asset.getTotalSupply();
    }

    function updateTotalSupply(uint256 _totalBalances) public onlyCreator returns(bool){
        AssetData asset=AssetData(assetData);
        asset.updateTotalSupply(_totalBalances);
        return true;
    }

    //查询账户余额
    function balanceByAccount(address _externalAccount) isExistAccount(_externalAccount) public view returns (uint256[] memory detail){
        address userAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        detail=asset.getBalance(userAccount);
    }

    //T+N限售记录
    function getTransRestrictedDetailByAccount(address _externalAccount) isExistAccount(_externalAccount) public view returns(uint status,string memory result){
        address userAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        (status,result)=asset.getTransRestrictedDetailByAccount(userAccount);
    }

    function getPledgeDetailByNumber(address _externalAccount,uint256 _pledgeNumber) isExistAccount(_externalAccount) public view returns(uint statusCode, string[] memory retContent){
        address userAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        (statusCode,retContent)=asset.getPledgeDetailByNumber(userAccount,_pledgeNumber);
    }

    function getPledgeDetailByAccount(address _externalAccount) isExistAccount(_externalAccount) public view returns(uint statusCode, string memory retContent){
        address userAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        (statusCode ,retContent)=asset.getPledgeDetailByAccount(userAccount);
    }

    function getFrozenDetailByNumber(address _externalAccount,uint256 _frozenNumber) isExistAccount(_externalAccount) public view returns(uint statusCode, string[] memory retContent){
        address userAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        (statusCode,retContent)=asset.getFrozenDetailByNumber(userAccount,_frozenNumber);
    }

    function getFrozenDetailByAccount(address _externalAccount) isExistAccount(_externalAccount) public view returns(uint statusCode, string memory retContent){
        address userAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        (statusCode ,retContent)=asset.getFrozenDetailByAccount(userAccount);
    }

    //查询指定冻结编号的轮候冻结编号
    function getNextWaitingFrozenNumber(address _externalAccount,uint unFrozenNumber) public isExistAccount(_externalAccount) view returns(uint number){
        address toUserAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        number=asset.getNextWaitingFrozenNumber(toUserAccount,unFrozenNumber);
    }

    //转让交易
    //transferType:1-交易过户，2-非交易过户
    function transferByAdmin(address _externalFromAccount,address _externaltoAccount,uint amount,uint transferType) public checkContractStatus isExistAccount(_externaltoAccount)  returns (uint256){
        require(AccountManager(accountManager).hasAccount(_externalFromAccount), "sender account is not exist.");
        address toUserAccount=AccountManager(accountManager).getUserAccount(_externaltoAccount);
        address fromUserAccount=AccountManager(accountManager).getUserAccount(_externalFromAccount);
        AssetData asset=AssetData(assetData);
        uint256[] memory detail=asset.getBalance(fromUserAccount);
        require(detail[3]>=amount,"My balance is not enough");
        uint previousBlock;
        uint256 number;
        (previousBlock,number)=asset.transferIn(toUserAccount,amount,tradeRestrictDay.add(now/SECONDS_PER_DAY),transferType);
        string[] memory content=new string[](5);
        content[0]=fromUserAccount.addressToString();
        content[1]=toUserAccount.addressToString();
        content[2]=amount.uint2str();
        content[3]=number.uint2str();
        content[4]=dateTimeUtil.getDate(now/1000);
        emit StockBusinessRecord(transferType==1?TRADE_TRANSFER_OUT:NO_TRADE_TRANSFER_OUT,toUserAccount.addressToString(),content,previousBlock);
        previousBlock=asset.transferOut(fromUserAccount,amount);
        emit StockBusinessRecord(transferType==1?TRADE_TRANSFER_IN:NO_TRADE_TRANSFER_IN,fromUserAccount.addressToString(),content,previousBlock);
        return amount;
    }

    //T+N解除限售
    function cancelTransRestrictions(address _externalAccount,uint256[] _transNumber) public checkContractStatus isExistAccount(_externalAccount) returns(int8 count){
        address userAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        uint statusCode=2;
        string[] memory retContent;
        count=0;
        for(uint i=0;i<_transNumber.length;i++){
            (statusCode,retContent)=asset.getTransRestrictedDetailByNumber(userAccount,_transNumber[i]);
            if(statusCode==1 &&retContent[2].compare((now/SECONDS_PER_DAY).uint2str())==-1){
                asset.removeTransTimeLock(userAccount,_transNumber[i]);
                count++;
            }
        }
    }

    //增资
    //isRSales:0-不限售，1-限售
    function incrBalance(address _externalAccount, uint256 amount,uint isRSales) public checkContractStatus isExistAccount(_externalAccount)  returns(uint number){
        address userAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        uint256[] memory detail=asset.getBalance(userAccount);
        uint256[] memory dataUint=new uint256[](4);
        dataUint[0]=detail[0].add(amount);
        if(isRSales==1){
            uint256 incrRSales=amount.mul(rSalesRatio).div(100);
            dataUint[2]=detail[2].add(incrRSales);
            dataUint[1]=detail[1].add(amount.sub(incrRSales));
            dataUint[3]=detail[3].add(amount.sub(incrRSales));
        }else{
            dataUint[1]=detail[1].add(amount);
            dataUint[2]=detail[2];
            dataUint[3]=detail[3].add(amount);
        }
        number=0;
        uint previousBlock=0;
        (number,previousBlock)=asset.updateBalance(userAccount,dataUint);

        string[] memory content=new string[](4);
        content[0]=userAccount.addressToString();
        content[1]=amount.uint2str();
        content[2]=dateTimeUtil.getDate(now/1000);
        content[3]=number.uint2str();
        emit StockBusinessRecord(INCR_CAPITAL_TYPE,userAccount.addressToString(),content,previousBlock);

        return number;
    }

    //减资
    function reduceBalance(address _externalAccount, uint256 amount) public checkContractStatus isExistAccount(_externalAccount)  returns(uint number){
        address userAccount=AccountManager(accountManager).getUserAccount(_externalAccount);
        AssetData asset=AssetData(assetData);
        uint256[] memory detail=asset.getBalance(userAccount);

        require(detail[3]>=amount, "canTransaction amount is not enough");

        uint256[] memory dataUint=new uint256[](4);
        dataUint[0]=detail[0].sub(amount);
        dataUint[1]=detail[1].sub(amount);
        dataUint[2]=detail[2];
        dataUint[3]=detail[3].sub(amount);

        number=0;
        uint previousBlock=0;
        (number,previousBlock)=asset.updateBalance(userAccount,dataUint);

        string[] memory content=new string[](4);
        content[0]=userAccount.addressToString();
        content[1]=amount.uint2str();
        content[2]=dateTimeUtil.getDate(now/1000);
        content[3]=number.uint2str();
        emit StockBusinessRecord(REDUCE_CAPITAL_TYPE,userAccount.addressToString(),content,previousBlock);
        return number;
    }
    //质押
    //balanceData[0]=pledgeAmount, balanceData[1]=pledgeTransRestrictedSales,balanceData[2]=pledgeCirculate,
    //balanceData[3]=pledgeRestrictedSales,balanceData[4]=canTrans(可交易总额）
    //pledgeDetail: pledgor,pledgee,amount,fromCirculate,fromRestrictedSales,releaseTime
    function doPledge(uint256[] balanceData,string[] pledgeDetail,string causeDesc) public checkContractStatus returns(uint256 _pledgeNumber){
        require(AccountManager(accountManager).hasAccount(pledgeDetail[0].stringToAddress()), "the externalAccount of pledgor is not exist.");
        require(AccountManager(accountManager).hasAccount(pledgeDetail[1].stringToAddress()), "the externalAccount of pledgee is not exist.");
        address pledgor=AccountManager(accountManager).getUserAccount(pledgeDetail[0].stringToAddress());
        address pledgee=AccountManager(accountManager).getUserAccount(pledgeDetail[1].stringToAddress());
        pledgeDetail[0]=pledgor.addressToString();
        pledgeDetail[1]=pledgee.addressToString();
        AssetData asset=AssetData(assetData);
        uint previousBlock=asset.updatePledge(pledgor,balanceData);
        _pledgeNumber=asset.addPledgeDetail(pledgeDetail);
        string[] memory content=new string[](9);
        content[0]=pledgor.addressToString();
        content[1]=pledgee.addressToString();
        content[2]=_pledgeNumber.uint2str();
        content[3]=pledgeDetail[2];
        content[4]=pledgeDetail[3];
        content[5]=pledgeDetail[4];
        content[6]=pledgeDetail[5];
        content[7]=dateTimeUtil.getDate(now/1000);
        content[8]=causeDesc;
        emit StockBusinessRecord(PLEDGE_TYPE,pledgor.addressToString(),content,previousBlock);
    }

    //解押
    //balanceData[0]=pledgeAmount, balanceData[1]=pledgeTransRestrictedSales,balanceData[2]=pledgeCirculate,balanceData[3]=pledgeRestrictedSales,balanceData[4]=canTrans(可交易总额）
    function undoPledge(address pledgor,uint256[] balanceData,uint256 _pledgeNumber) public checkContractStatus isExistAccount(pledgor)  returns(bool){
        address inter_pledgor=AccountManager(accountManager).getUserAccount(pledgor);
        AssetData asset=AssetData(assetData);
        uint statusCode;
        string[] memory retContent;
        (statusCode,retContent)=asset.getPledgeDetailByNumber(inter_pledgor,_pledgeNumber);
        if(statusCode==SUCCESS_RETURN){
            uint previousBlock=asset.updatePledge(inter_pledgor,balanceData);
            asset.removePledgeDetail(inter_pledgor,_pledgeNumber);

            string[] memory content=new string[](9);
            content[0]=inter_pledgor.addressToString();
            content[1]=retContent[1];
            content[2]=_pledgeNumber.uint2str();
            content[3]=retContent[2];
            content[4]=retContent[3];
            content[5]=retContent[4];
            content[6]=retContent[5];
            content[7]=dateTimeUtil.getDate(now/1000);
            content[8]="";
            emit StockBusinessRecord(RELEASE_PLEDGE_TYPE,inter_pledgor.addressToString(),content,previousBlock);
            return true;
        }else{
            return false;
        }
    }

    //冻结
    //balanceData[0]=freeze(总冻结额）, balanceData[1]=waitingFreeze(等待冻结总额）,balanceData[2]=frozenTransRestrictedSales(冻结交易限售的总额）
    //balanceData[3]=frozenCirculate(冻结流通股的总额）,balanceData[4]=frozenRestrictedSales(冻结限售股的总额）, balanceData[5]=canTrans(可交易总额）
    //frozenDetail: assetOwnerAddress,businessType,applicant,requestAmount,frozenAmount,waitingAmount,startTime,endTime,waitingNumber,status
    //1-司法冻结，2-手工冻结,3--轮候冻结
    function doFrozen(uint256[] balanceData,string[] frozenDetail) public checkContractStatus isExistAccount(frozenDetail[0].stringToAddress()) returns(uint256 _frozenNumber){
        address assetOwnerAddress=AccountManager(accountManager).getUserAccount(frozenDetail[0].stringToAddress());
        AssetData asset=AssetData(assetData);
        if(frozenDetail[8].str2uint()>0){
            uint statusCode;
            string[] memory retContent;
            (statusCode,retContent)=asset.getFrozenDetailByNumber(assetOwnerAddress,frozenDetail[8].str2uint());
            uint number=asset.getNextWaitingFrozenNumber(assetOwnerAddress,frozenDetail[8].str2uint());
            require(statusCode==SUCCESS_RETURN,"this waitingNumber frozen is not exist");
            require(number==0,"already have waiting frozen under the waitingNumber");
        }
        frozenDetail[0]=assetOwnerAddress.addressToString();
        uint previousBlock=asset.updateFreeze(assetOwnerAddress,balanceData);
        _frozenNumber=asset.addFrozenDetail(frozenDetail);
        uint businessType=WAITING_FREEZING;
        if(frozenDetail[1].compare("1")==0){
            businessType=JUDICIAL_FREEZING;
        }else if(frozenDetail[1].compare("2")==0){
            businessType=MANUAL_FREEZING;
        }
        frozenDetail[1]=businessType.uint2str();
        frozenDetail[0]=_frozenNumber.uint2str();
        emit StockBusinessRecord(businessType,assetOwnerAddress.addressToString(),frozenDetail,previousBlock);
    }

    //轮候冻结转冻结
    //balanceData[0]=freeze, balanceData[1]=waitingFreeze,balanceData[2]=frozenTransRestrictedSales,balanceData[3]=frozenCirculate,balanceData[4]=frozenRestrictedSales, balanceData[5]=canTrans(可交易总额）
    //frozenDetail: serialNum,businessType,applicant,requestAmount,frozenAmount,waitingAmount,startTime,endTime,waitingNumber,status
    function doWaitingFrozenToFrozen(uint256[] balanceData,string _ownerAddress,string[] frozenDetail) public checkContractStatus isExistAccount(_ownerAddress.stringToAddress()) returns(bool){
        require(balanceData[2].add(balanceData[3]).add(balanceData[4])==balanceData[0],"The total freeze amount is inconsistent with the freeze details");
        require(balanceData.length==6,"the length of balanceData should 6");
        address assetOwnerAddress=AccountManager(accountManager).getUserAccount(_ownerAddress.stringToAddress());
        AssetData asset=AssetData(assetData);
        uint statusCode;
        string[] memory retContent;
        (statusCode,retContent)=asset.getFrozenDetailByNumber(assetOwnerAddress,frozenDetail[0].str2uint());
        if(statusCode==SUCCESS_RETURN){
            uint previousBlock=asset.updateFreeze(assetOwnerAddress,balanceData);
            asset.updateFrozenDetail(assetOwnerAddress.addressToString(),frozenDetail);
            frozenDetail[1]=WAITING_FREEZING_FROZEN.uint2str();
            emit StockBusinessRecord(WAITING_FREEZING_FROZEN,assetOwnerAddress.addressToString(),frozenDetail,previousBlock);
            return true;
        }
        return false;
    }

    //解冻
    //balanceData[0]=freeze, balanceData[1]=waitingFreeze,balanceData[2]=frozenTransRestrictedSales,balanceData[3]=frozenCirculate,balanceData[4]=frozenRestrictedSales, balanceData[5]=canTrans(可交易总额）
    function undoFrozen(address assetOwnerAddress,uint256[] balanceData,uint256 _frozenNumber) public checkContractStatus isExistAccount(assetOwnerAddress)  returns(uint waitingNumber){
        address interAccount=AccountManager(accountManager).getUserAccount(assetOwnerAddress);
        AssetData asset=AssetData(assetData);
        uint statusCode;
        string[] memory retContent;
        (statusCode,retContent)=asset.getFrozenDetailByNumber(interAccount,_frozenNumber);
        waitingNumber=0;
        if(statusCode==SUCCESS_RETURN){
            uint previousBlock=asset.updateFreeze(interAccount,balanceData);
            asset.removeFrozenDetail(interAccount,_frozenNumber);
            waitingNumber=asset.getNextWaitingFrozenNumber(interAccount,_frozenNumber);
            retContent[1]=UNFREEZE.uint2str();
            retContent[8]="-1";
            retContent[9]=RELEASE_FREEZE.uint2str();

            emit StockBusinessRecord(UNFREEZE,interAccount.addressToString(),retContent,previousBlock);
            return waitingNumber;
        }
    }

    function getLastBlockRecord(address account) isExistAccount(account) public view returns(uint256 blockNumber){
        address interAccount=AccountManager(accountManager).getUserAccount(account);
        blockNumber=AssetData(assetData).getLastBlockRecord(interAccount);
    }

    function getAssetDataAddress()public view returns(address){
        return assetData;
    }

    function setAssetDataAddress(address _assetData)public onlyGovernor {
        assetData=_assetData;
    }

    function getAddress() public view returns (address) {
        return address(this);
    }

}