pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./lib/Table.sol";
import "./lib/Strings.sol";
import "./lib/StringUtil.sol";
import "./Constant.sol";

/*
* 使用分布式存储下表结构、CURD工具以及预定义常数
*
*/
contract TableDefTools is Constant {

    /*******   引入库  *******/
    using Strings for *;
    using StringUtil for *;

    /******* 表结构体 *******/
    struct Bean {
        // 表名称
        string tableName;
        // 主键
        string primaryKey;
        // 表字段
        string[] fields;
    }

    //fields的索引ID  mapping(tableName=>mapping(field=>index))
    mapping(string => mapping(string => uint))  fieldsIndex;

    /******* 使用到的表结构 *******/
    Bean  t_transaction_struct;
    Bean  t_pledge_struct;
    Bean  t_frozen_struct;

    // 新增记录的事件
    event InsertRecord(string tableName,string primaryKey,string fields);
    // 更新记录的事件
    event UpdateRecord(string tableName,string primaryKey,string fields);

    //更新错误的日志
    event UpdateRecordError(string tableName,string primaryKey,string fields,string msg);

    event Debug(string msg);

    // 交易记录表
    // 表名称：t_transaction
    // 表主键：account
    // 表字段：serial_num,amount,releaseTime;
    string constant internal TABLE_TRANSACTION_NAME = "t_transaction_14";
    string constant internal TABLE_TRANSACTION_KEY = "account";
    string constant internal TABLE_TRANSACTION_FIELDS = "serialNum,amount,releaseTime";

    // 质押记录表
    // 表名称：t_pledge
    // 表主键：pledgor
    // 表字段：serial_num,pledgee,amount,fromCirculate,fromRestrictedSales,releaseTime
    string constant internal TABLE_PLEDGE_NAME = "t_pledge_14";
    string constant internal TABLE_PLEDGE_KEY = "pledgor";
    string constant internal TABLE_PLEDGE_FIELDS = "serialNum,pledgee,amount,fromCirculate,fromRestrictedSales,releaseTime";

    // 冻结记录表
    // 表名称：t_frozen
    // 表主键：assetOwnerAddress
    // 表字段：serial_num,businessType,applicant,requestAmount,frozenAmount,waitingAmount,startTime,endTime,waitingNumber,status
    //businessType: 1-司法冻结，2-手工冻结,3--轮候冻结
    //status: 1-全部冻结，2-部分冻结，3-全部轮候，4-已解冻
    string constant internal TABLE_FROZEN_NAME = "t_frozen_14";
    string constant internal TABLE_FROZEN_KEY = "assetOwnerAddress";
    string constant internal TABLE_FROZEN_FIELDS = "serialNum,businessType,applicant,requestAmount,frozenAmount,waitingAmount,startTime,endTime,waitingNumber,status";

    /*
    * 字符串数组生成工具
    *
    * @param _fields  带解析的字符串
    *
    * @return 解析后的字符串数组
    *
    */
    function getFieldsArray(string _fields) internal pure returns(string[]){
        string[] memory arrays ;
        Strings.slice memory s = _fields.toSlice();
        Strings.slice memory delim = ",".toSlice();
        uint params_total = s.count(delim) + 1;
        arrays = new string[](params_total);
        for(uint i = 0; i < params_total; i++) {
            arrays[i] = s.split(delim).toString();
        }
        return arrays;
    }


    /*
     * 初始化一个表结构
     *
     * @param _tableStruct  表结构体
     * @param _tableName    表名称
     * @param _primaryKey   表主键
     * @param _fields       表字段组成的字符串（除主键）
     *
     * @return 无
     *
     */
    function initTableStruct(
        Bean storage _tableStruct,
        string memory _tableName,
        string memory _primaryKey,
        string memory  _fields)
    internal {
        TableFactory tf = TableFactory(0x1001);
        tf.createTable(_tableName, _primaryKey, _fields);

        _tableStruct.tableName = _tableName;
        _tableStruct.primaryKey = _primaryKey;
        _tableStruct.fields = new string[](0);

        Strings.slice memory s = _fields.toSlice();
        Strings.slice memory delim = ",".toSlice();
        uint params_total = s.count(delim) + 1;
        for(uint i = 0; i < params_total; i++) {
            _tableStruct.fields.push(s.split(delim).toString());
        }
        for(uint j = 0; j < _tableStruct.fields.length; j++){
            fieldsIndex[_tableName][_tableStruct.fields[j]]=j+1;
        }
    }


    /*
     * 打开一个表，便于后续操作该表
     *
     * @param _tableName    表名称
     *
     * @return 表
     *
     */
    function openTable(string memory _table_name) internal returns(Table) {
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable(_table_name);
        return table;
    }


    /*
     * 查询表中一条记录并以Json格式输出
     *
     * @param _tableStruct    表结构
     * @param _primaryKey     待查记录的主键
     *
     * @return 执行状态码
     * @return 该记录的json字符串
     */
    function selectOneRecordToJson(Bean storage _tableStruct, string memory _primaryKey) internal view returns (uint, string memory) {
        // 打开表
        Table table = openTable(_tableStruct.tableName);
        Condition condition = table.newCondition();
        // 查询
        Entries entries = table.select(_primaryKey, condition);
        // 将查询结果解析为json字符串
        return StringUtil.getJsonString(_tableStruct.primaryKey,_tableStruct.fields, _primaryKey, entries);
    }

    /*
     * 查询表中一条记录并以字符串数组的格式输出
     *
     * @param _tableStruct    表结构
     * @param _primaryKey     待查记录的主键
     * @param _conditionPair  筛选条件（一个字段）
     *
     * @return 执行状态码(1: 成功，-2，空）
     * @return 该记录的字符串数组
     */
    function selectOneRecordToArray(Bean storage _tableStruct, string memory _primaryKey, string[]  _conditionPair) internal  returns (uint, string[] memory) {

        // 打开表
        Table table = openTable(_tableStruct.tableName);
        // 查询
        Condition condition = table.newCondition();
        if(_conditionPair.length == 2){
            condition.EQ(_conditionPair[0], _conditionPair[1]);
        }
        Entries entries = table.select(_primaryKey, condition);
        uint statusCode = 0;
        string[] memory retContent;
        if (entries.size() > 0) {
            statusCode = SUCCESS_RETURN;
            retContent = StringUtil.getEntry(_tableStruct.fields, entries.get(0));
        }else{
            statusCode = FAIL_NULL_RETURN;
            retContent = new string[](0);
        }
        return (statusCode ,retContent);
        // 将查询结果解析为json字符串

    }

    /*
    * 向指定表中插入一条记录
    *
    * @param _tableStruct    表结构
    * @param _primaryKey     待插记录的主键
    * @param _fields         各字段值
    * @param _isRepeatable   主键下记录是否可重复
    *
    * @return 执行状态码
    */
    function insertOneRecord(Bean storage _tableStruct, string memory _primaryKey, string[] memory _fields, bool _isRepeatable) internal returns (uint) {
        require(_tableStruct.fields.length==_fields.length,"the length of _fields is error");

        uint setStatus = INITIAL_STATE;
        uint getStatus = INITIAL_STATE;
        string memory getContent;

        Table table = openTable(_tableStruct.tableName);
        if(_isRepeatable){
            getStatus = FAIL_NULL_RETURN;
        }else{
            (getStatus, getContent) = selectOneRecordToJson(_tableStruct, _primaryKey);
        }
        if (getStatus == FAIL_NULL_RETURN) {
            // 创建表记录
            Entry entry = table.newEntry();
            entry.set(_tableStruct.primaryKey, _primaryKey);
            for (uint i = 0; i < _tableStruct.fields.length; i++) {
                entry.set(_tableStruct.fields[i], _fields[i]);
            }
            setStatus = FAIL_INSERT;
            //新增表记录
            if (table.insert(_primaryKey, entry) == 1) {
                setStatus = SUCCESS_RETURN;
                //emit InsertRecord(_tableStruct.tableName,_primaryKey,_fields);
            } else {
                setStatus = FAIL_RETURN;
            }
        } else {
            setStatus = FAIL_ALREADY_EXIST;
        }
        return  setStatus;
    }


    /*
     * 向指定表中更新一条记录
     *
     * @param _tableStruct    表结构
     * @param _primaryKey     待更新记录的主键
     * @param _fields         各字段值组成的字符串
     * @param _conditionPair  筛选条件（一个字段）
     *
     * @return 执行状态码
     */
    function updateOneRecord(Bean storage _tableStruct, string memory _primaryKey, string[] memory _fields,string[]  _conditionPair) internal returns(uint) {
        require(_tableStruct.fields.length==_fields.length,"the length of _fields is error");
        Table table = openTable(_tableStruct.tableName);

        Entry entry = table.newEntry();
        Condition condition = table.newCondition();
        if(_conditionPair.length == 2){
            condition.EQ(_conditionPair[0], _conditionPair[1]);
        }
        entry.set(_tableStruct.primaryKey, _primaryKey);

        for (uint i = 0; i < _tableStruct.fields.length; i++) {
            entry.set(_tableStruct.fields[i], _fields[i]);
        }

        int count = table.update(_primaryKey, entry, condition);

        if(count == 1){
            //emit UpdateRecord(_tableStruct.tableName,_primaryKey,_fields);
            return SUCCESS_RETURN;
        }else{
            return FAIL_RETURN;
        }

    }

    /*
     * 向指定表中删除记录
     *
     * @param _tableStruct    表结构
     * @param _primaryKey     待查记录的主键
     * @param _conditionPair  筛选条件（一个字段）
     *
     * @return 执行状态码
     */
    function removeRecord(Bean storage _tableStruct, string memory _primaryKey, string[] memory _conditionPair) internal returns(uint) {
        Table table = openTable(_tableStruct.tableName);
        Condition condition = table.newCondition();
        if(_conditionPair.length == 2){
            condition.EQ(_conditionPair[0], _conditionPair[1]);
        }
        int count = table.remove(_primaryKey, condition);

        if(count >=0){
            return SUCCESS_RETURN;
        }else{
            return FAIL_RETURN;
        }
    }

}
