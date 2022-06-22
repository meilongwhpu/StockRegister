pragma solidity ^0.4.24;
pragma experimental ABIEncoderV2;


contract Constant {

    uint256 constant SECONDS_PER_DAY = 86400000;

    //股份公司
    uint constant STOCK_CORP=1;
    //非股份公司
    uint constant LIMITED_CORP=2;

    //增资
    uint constant INCR_CAPITAL_TYPE=1;
    //减资
    uint constant REDUCE_CAPITAL_TYPE=2;
    //交易过户转入
    uint constant TRADE_TRANSFER_IN=3;
    //交易过户转出
    uint constant TRADE_TRANSFER_OUT=4;
    //非交易过户转入
    uint constant NO_TRADE_TRANSFER_IN=5;
    //非交易过户转出
    uint constant NO_TRADE_TRANSFER_OUT=6;
    //质押
    uint constant PLEDGE_TYPE=7;
    //解押
    uint constant RELEASE_PLEDGE_TYPE=8;
    //司法冻结
    uint constant JUDICIAL_FREEZING=9;
    //手工冻结
    uint constant MANUAL_FREEZING=10;
    //轮候冻结
    uint constant WAITING_FREEZING=11;
    //轮候冻结转冻结
    uint constant WAITING_FREEZING_FROZEN=12;
    //解冻
    uint constant UNFREEZE=13;

    //1-全部冻结
    uint constant FREEZE_ALL=1;
    //2-部分冻结
    uint constant FREEZE_PART=2;
    //3-全部轮候
    uint constant WAITING_FREEZE_ALL=3;
    //4-已解冻
    uint constant RELEASE_FREEZE=4;

    address constant EMPTY_ADDR= 0x0;

    /******* 执行状态码常量 *******/
    uint constant INITIAL_STATE = 0;
    uint constant SUCCESS_RETURN = 1;
    uint constant FAIL_RETURN = 2;
    uint constant FAIL_NULL_RETURN = 3;
    uint constant FAIL_ALREADY_EXIST = 4;
    uint constant FAIL_INSERT = 5;

}