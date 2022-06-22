package com.cstc.stockregister.constant;

public interface SysConstant {
    /**
     * 默认群组ID
     */
    int DEFAULT_GROUP_ID=1;

    String EMPTY_ADDRESS="0x0000000000000000000000000000000000000000";
    //股份公司
    int STOCK_CORP=1;
    //非股份公司
    int LIMITED_CORP=2;

    //所有业务记录
    int ALL_RECORD_TYPE=0;
    //资本变更记录
    int CAPITAL_CHANGE_RECORD_TYPE=1;
    //交易记录
    int TRANSFER_RECORD_TYPE=2;
    //质押解押记录
    int PLEDGE_RECORD_TYPE=3;
    //冻结解冻
    int FROZEN_RECORD_TYPE=4;

    //增资
    int INCR_CAPITAL_TYPE=1;
    //减资
    int REDUCE_CAPITAL_TYPE=2;
    //交易过户转入
    int TRADE_TRANSFER_IN=3;
    //交易过户转出
    int TRADE_TRANSFER_OUT=4;
    //非交易过户转入
    int NO_TRADE_TRANSFER_IN=5;
    //非交易过户转出
    int NO_TRADE_TRANSFER_OUT=6;
    //质押
    int PLEDGE_TYPE=7;
    //解押
    int RELEASE_PLEDGE_TYPE=8;
    //司法冻结
    int JUDICIAL_FREEZING=9;
    //手工冻结
    int MANUAL_FREEZING=10;
    //轮候冻结
    int WAITING_FREEZING=11;
    //轮候冻结转冻结
    int WAITING_FREEZING_FROZEN=12;
    //解冻
    int UNFREEZE=13;

    //1-全部冻结
    int FREEZE_ALL=1;
    //2-部分冻结
    int FREEZE_PART=2;
    //3-全部轮候
    int WAITING_FREEZE_ALL=3;
    //4-已解冻
    int RELEASE_FREEZE=4;

    //查询CURD合约的状态
    int QUERY_CURD_SUCCESS = 1;
}
