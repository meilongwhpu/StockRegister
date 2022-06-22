pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./lib/LibSafeMath.sol";
import "./lib/UtilLib.sol";
import "./TableDefTools.sol";
import "./lib/LibTypeConversion.sol";
import "./storage/AddressSetLib.sol";


contract AssetData is TableDefTools {

    using AddressSetLib for AddressSetLib.Set;
    using LibSafeMath for uint256;
    using LibTypeConversion for *;

    //0-governor(治理合约地址),1-owner(合约拥着者),2-creator(创建合约的合约地址)
    address[] addressList;

    //0-assetCode,1-assetName
    bytes[] assetAttrValues;

    uint256 totalBalances;

    uint256  currentNumber;
    //持仓账户列表
    AddressSetLib.Set accountSet;

    // 账户地址 => 资产明细
    //0-total;1-circulate;2-restrictedSales;3-canTrans;4-transRestrictedSales;5-pledgeAmount;6-freezeAmount;7-waitingFreeze;
    //8-frozenTransRestrictedSales;9-frozenCirculate;10-frozenRestrictedSales;
    //11-pledgeTransRestrictedSales;12-pledgeCirculate;13-pledgeRestrictedSales;
    mapping(address=>uint256[14]) balances;

    //存储资产的所有交易记录（交易、非交易过户、质押、冻结、解押、解冻）
    //value is user address ,key is block number
    mapping(address => uint) businessRecord;

    //冻结编号
    mapping(address =>uint) frozenNumber;
    //轮候冻结的等待顺序列表  privNumber --> nextNumber
    mapping(address =>mapping(uint =>uint)) frozenNumberLinked;


    //用于数据修正的权限控制（慎用）
    modifier onlyOwner() {
        require(tx.origin == address(addressList[1]) , "AssetData:only owner is authorized.");
        _;
    }

    //修改登记数据权限控制
    modifier onlyCreator() {
        require(msg.sender == address(addressList[2]),"AssetData:only creator is authorized.");
        _;
    }

    //修改管理合约地址
    modifier onlyGovernor() {
        require(msg.sender == address(addressList[0]), "AssetData:only governor is authorized.");
        _;
    }

    //_owner=创建StockAsset的tx.origin
    //dataStr[0]=assetCode,dataStr[1]=assetName
    function AssetData(address _owner,address _governor,bytes[] dataStr,uint256 _totalBalances) public {
        addressList.push(_governor);
        addressList.push(_owner);
        addressList.push(msg.sender);

        assetAttrValues.push(dataStr[0]);
        assetAttrValues.push(dataStr[1]);
        totalBalances=_totalBalances;
        initTableStruct(t_transaction_struct, TABLE_TRANSACTION_NAME, TABLE_TRANSACTION_KEY, TABLE_TRANSACTION_FIELDS);
        initTableStruct(t_pledge_struct, TABLE_PLEDGE_NAME, TABLE_PLEDGE_KEY, TABLE_PLEDGE_FIELDS);
        initTableStruct(t_frozen_struct, TABLE_FROZEN_NAME, TABLE_FROZEN_KEY, TABLE_FROZEN_FIELDS);
    }

    function getAssetAttrValue() public view returns (bytes[]){
        return assetAttrValues;
    }

    function getTotalSupply() public view returns (uint256) {
        return totalBalances;
    }

    function updateTotalSupply(uint256 _totalBalances) onlyCreator public{
        totalBalances=_totalBalances;
    }

    //持仓人数
    function getHolders() public view returns (uint){
        return accountSet.size();
    }

    //持仓人
    function getHoldersList() public view returns (address[]){
        return accountSet.getAll();
    }

    function hasAccount(address account) public view returns (bool){
        return accountSet.contains(account);
    }


    //更新投资者的事件日志链最新区块编号
    function getLastBlockRecord(address account) public view returns(uint256 blockNumber){
        blockNumber=businessRecord[account];
    }

    //queryBalance
    //returns total，circulate，restrictedSales，canTrans，transRestrictedSales,pledge，freeze，waitingFreeze
    //frozenTransRestrictedSales,frozenCirculate,frozenRestrictedSales,pledgeTransRestrictedSales,pledgeCirculate,pledgeRestrictedSales
    function getBalance(address account) public view returns (uint256[] memory detail){
        detail = new uint256[](14);
        detail[0]=balances[account][0];
        detail[1]=balances[account][1];
        detail[2]=balances[account][2];
        detail[3]=balances[account][3];
        detail[4]=balances[account][4];
        detail[5]=balances[account][5];
        detail[6]=balances[account][6];
        detail[7]=balances[account][7];

        detail[8]=balances[account][8];
        detail[9]=balances[account][9];
        detail[10]=balances[account][10];

        detail[11]=balances[account][11];
        detail[12]=balances[account][12];
        detail[13]=balances[account][13];
    }

    //转入
    //transferType:1-交易过户，2-非交易过户
    function transferIn(address toAccount, uint256 amount,uint256 releaseTime,uint transferType) public onlyCreator returns(uint previousBlock,uint256 number){
        balances[toAccount][0]=balances[toAccount][0].add(amount);
        balances[toAccount][1]=balances[toAccount][1].add(amount);
        //加入限售队列中
        number=getCurrentNumber();
        if(transferType==1){
            string[] memory _fields=new string[](3);
            _fields[0]=number.uint2str();
            _fields[1]=amount.uint2str();
            _fields[2]=releaseTime.uint2str();
            insertOneRecord(t_transaction_struct, toAccount.addressToString(), _fields,true);
            balances[toAccount][4]=balances[toAccount][4].add(amount);
        }else{
            balances[toAccount][3]=balances[toAccount][3].add(amount);
        }
        previousBlock=getCurrentBlock(toAccount);
    }

    //转出
    function transferOut(address fromAccount, uint256 amount) public onlyCreator returns(uint previousBlock){
        require(balances[fromAccount][3]>=amount, "canTransaction amount is not enough");
        balances[fromAccount][0]=balances[fromAccount][0].sub(amount);
        balances[fromAccount][1]=balances[fromAccount][1].sub(amount);
        balances[fromAccount][3]=balances[fromAccount][3].sub(amount);
        previousBlock=getCurrentBlock(fromAccount);
    }


    //更新投资者的冻结额
    //dataUint[0]=freeze, dataUint[1]=waitingFreeze,dataUint[2]=frozenTransRestrictedSales,dataUint[3]=frozenCirculate,dataUint[4]=frozenRestrictedSales,dataUint[5]=canTrans
    function updateFreeze(address account,uint256[] dataUint) public onlyCreator returns(uint previousBlock){
        balances[account][6]=dataUint[0];
        balances[account][7]=dataUint[1];

        balances[account][8]=dataUint[2];
        balances[account][9]=dataUint[3];
        balances[account][10]=dataUint[4];

        balances[account][3]=dataUint[5];
        previousBlock=getCurrentBlock(account);
    }

    //更新投资者的质押额
    //dataUint[0]=pledgeAmount, dataUint[1]=pledgeTransRestrictedSales,dataUint[2]=pledgeCirculate,dataUint[3]=pledgeRestrictedSales,balanceData[4]=canTrans(可交易总额）
    function updatePledge(address account,uint256[] dataUint) public onlyCreator returns(uint previousBlock){
        balances[account][5]=dataUint[0];

        balances[account][11]=dataUint[1];
        balances[account][12]=dataUint[2];
        balances[account][13]=dataUint[3];

        balances[account][3]=dataUint[4];
        previousBlock=getCurrentBlock(account);
    }

    //增资或减资
    //dataUint[0]=total,dataUint[1]=circulate,dataUint[2]=restrictedSales,dataUint[3]=canTrans
    function updateBalance(address account, uint256[] dataUint)  public onlyCreator returns(uint number,uint previousBlock){
        number=getCurrentNumber();
        balances[account][0]=dataUint[0];
        balances[account][1]=dataUint[1];
        balances[account][2]=dataUint[2];
        balances[account][3]=dataUint[3];
        previousBlock=getCurrentBlock(account);
    }

    //添加投资者质押记录
    //_fields: pledgor,pledgee,amount,fromCirculate,fromRestrictedSales,releaseTime
    function addPledgeDetail(string[] _fields)  public onlyCreator returns(uint256 _pledgeNumber){
        string memory pledgor=_fields[0];
        _pledgeNumber=getCurrentNumber();
        _fields[0]=_pledgeNumber.uint2str();
        insertOneRecord(t_pledge_struct,pledgor, _fields,true);
        //businessRecord[_fields[0].stringToAddress()]=block.number;
    }

    function removePledgeDetail(address _account,uint256 _pledgeNumber) public onlyCreator returns(uint){
        string[] memory _conditionPair=new string[](2);
        _conditionPair[0]="serialNum";
        _conditionPair[1]=_pledgeNumber.uint2str();
        //businessRecord[_account]=block.number;
        return removeRecord(t_pledge_struct, _account.addressToString(), _conditionPair);
    }

    function removeFrozenDetail(address _account,uint256 _frozenNumber) public  onlyCreator  returns(uint){
        string[] memory _conditionPair=new string[](2);
        _conditionPair[0]="serialNum";
        _conditionPair[1]=_frozenNumber.uint2str();
        //businessRecord[_account]=block.number;
        return removeRecord(t_frozen_struct,_account.addressToString(), _conditionPair);
    }

    //添加投资者冻结记录
    //_fields:_assetOwnerAddress,businessType,_applicant,requestAmount,frozenAmount,waitingAmount,startTime,endTime,waitingNumber,status
    function addFrozenDetail(string[] _fields) public  onlyCreator returns(uint256 _frozenNumber){
        string memory _assetOwnerAddress=_fields[0];
        _frozenNumber=getCurrentNumber();
        _fields[0]=_frozenNumber.uint2str();
        insertOneRecord(t_frozen_struct, _assetOwnerAddress , _fields,true);
        if(_fields[8].str2uint()>0){
            frozenNumberLinked[_assetOwnerAddress.stringToAddress()][_fields[8].str2uint()]=_frozenNumber;
        }
        frozenNumber[_assetOwnerAddress.stringToAddress()]=currentNumber;
        //businessRecord[_fields[0].stringToAddress()]=block.number;
    }

    //更新投资者冻结记录（用于轮候冻结）
    //_fields:serial_num,businessType,applicant,requestAmount,frozenAmount,waitingAmount,startTime,endTime,waitingNumber,status
    function updateFrozenDetail(string _assetOwnerAddress,string []_fields) public  onlyCreator returns(bool){
        string[] memory _conditionPair=new string[](2);
        _conditionPair[0]="serialNum";
        _conditionPair[1]=_fields[0];
        updateOneRecord(t_frozen_struct, _assetOwnerAddress, _fields,_conditionPair);
        return true;
    }

    function removeTransTimeLock(address _account,uint256 _number)  public onlyCreator returns(uint){
        string[] memory _conditionPair=new string[](2);
        _conditionPair[0]="serialNum";
        _conditionPair[1]=_number.uint2str();
        return removeRecord(t_transaction_struct, _account.addressToString(), _conditionPair);
    }

    //更新投资者的流通股
    function updateCirculate(address account, uint256 circulate) public onlyOwner {
        balances[account][1]=circulate;
    }

    //更新投资者的限售股
    function updateRestrictedSales(address account, uint256 restrictedSales) public onlyOwner {
        balances[account][2]=restrictedSales;
    }

    //更新投资者的可交易额
    function updateCanTrans(address account, uint256 canTrans) public onlyOwner {
        balances[account][3]=canTrans;
    }

    //更新投资者的交易限售额
    function updateTransRestrictedSales(address account, uint256 transRestrictedSales) public onlyOwner {
        balances[account][4]=transRestrictedSales;
    }

    //更新投资者的轮候冻结额
    function updateWaitingFreeze(address account, uint256 waitingFreeze) public onlyOwner {
        balances[account][7]=waitingFreeze;
    }

    //查询投资者可质押余额
    function canPledgeAmount(address _pledgor) public view returns(uint256 amount){
        uint256 frzoen =balances[_pledgor][6].add(balances[_pledgor][7]);
        amount=balances[_pledgor][0].sub(frzoen).sub(balances[_pledgor][5]);
    }

    //获取投资者质押数据
    //retContent[0]=serial_num,retContent[1]=pledgee,retContent[2]=amount,retContent[3]=fromCirculate,retContent[4]=fromRestrictedSales,retContent[5]=releaseTime
    function getPledgeDetailByNumber(address _pledgor,uint256 _pledgeNumber) public view  returns(uint statusCode, string[] memory retContent){
        string[]  memory _conditionPair=new string[](2);
        _conditionPair[0]="serialNum";
        _conditionPair[1]=_pledgeNumber.uint2str();
        (statusCode ,retContent)=selectOneRecordToArray(t_pledge_struct,_pledgor.addressToString(),_conditionPair);
    }

    function getPledgeDetailByAccount(address _pledgor) public view returns(uint statusCode, string memory retContent){
        (statusCode ,retContent)=selectOneRecordToJson(t_pledge_struct,_pledgor.addressToString());
    }

    //获取投资者质押记录
    function getFrozenDetailByNumber(address _account,uint256 _frozenNumber) public view  returns(uint statusCode, string[] memory retContent){
        string[] memory _conditionPair=new string[](2);
        _conditionPair[0]="serialNum";
        _conditionPair[1]=_frozenNumber.uint2str();
        (statusCode ,retContent)=selectOneRecordToArray(t_frozen_struct,_account.addressToString(),_conditionPair);
    }

    function getFrozenDetailByAccount(address _account) public view  returns(uint statusCode, string memory retContent){
        (statusCode ,retContent)=selectOneRecordToJson(t_frozen_struct,_account.addressToString());
    }


    //获取投资者当前冻结业务之后的轮候冻结编号
    function getNextWaitingFrozenNumber(address account,uint _frozenNumber) public view returns(uint waitingNumber){
        waitingNumber=frozenNumberLinked[account][_frozenNumber];
    }

    //获取投资者的T+N交易限售的明细
    function getTransRestrictedDetailByAccount(address account) public view  returns(uint status, string memory detail){
        (status,detail)=selectOneRecordToJson(t_transaction_struct,account.addressToString());
    }

    //获取投资者T+N交易限售记录
    //@return 执行状态码(1: 成功，-2，空）
    // @return 该记录的字符串数组
    function getTransRestrictedDetailByNumber(address _account,uint256 _transNumber)  public view returns(uint statusCode, string[] memory retContent){
        string[] memory _conditionPair=new string[](2);
        _conditionPair[0]="serialNum";
        _conditionPair[1]=_transNumber.uint2str();
        (statusCode ,retContent)=selectOneRecordToArray(t_transaction_struct,_account.addressToString(),_conditionPair);
    }

    function getGovernor() public view returns (address) {
        return addressList[0];
    }

    function getCreator() public view returns (address) {
        return addressList[2];
    }

    function getOwner() public view returns (address) {
        return addressList[1];
    }

    function setOwner(address _owner)  public onlyGovernor  {
        addressList[1]=_owner;
    }

    function setGovernor(address _governor) public onlyGovernor  {
        addressList[0]=_governor;
    }

    function setCreator(address _creator) public onlyGovernor {
        addressList[2]=_creator;
    }

    //当前业务序号
    function getCurrentNumber() internal returns(uint256){
        currentNumber = currentNumber.add(1);
        return currentNumber;
    }

    function getCurrentBlock(address account) internal returns(uint previousBlock){
        previousBlock=businessRecord[account];
        businessRecord[account]=block.number;
    }

    //获取投资者上一笔冻结业务的编号
    function getPrivFrozenNumber(address account) internal view returns(uint number){
        number=frozenNumber[account];
    }

    function getAddress() public view returns (address) {
        return address(this);
    }
}