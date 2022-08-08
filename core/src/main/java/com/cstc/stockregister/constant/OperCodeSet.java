package com.cstc.stockregister.constant;

import java.util.HashSet;

public class OperCodeSet {
    public static  HashSet<Integer> capitalChangeTypeSet=new HashSet<>();
    public static   HashSet<Integer> tranferTypeSet=new HashSet<>();
    public static  HashSet<Integer> pledgeTypeSet=new HashSet<>();
    public static  HashSet<Integer> frozenTypeSet=new HashSet<>();

    static  {
        capitalChangeTypeSet.add(SysConstant.INCR_CAPITAL_TYPE);
        capitalChangeTypeSet.add(SysConstant.REDUCE_CAPITAL_TYPE);

        tranferTypeSet.add(SysConstant.TRADE_TRANSFER_IN);
        tranferTypeSet.add(SysConstant.TRADE_TRANSFER_OUT);
        tranferTypeSet.add(SysConstant.NO_TRADE_TRANSFER_IN);
        tranferTypeSet.add(SysConstant.NO_TRADE_TRANSFER_OUT);

        pledgeTypeSet.add(SysConstant.PLEDGE_TYPE);
        pledgeTypeSet.add(SysConstant.RELEASE_PLEDGE_TYPE);

        frozenTypeSet.add(SysConstant.JUDICIAL_FREEZING);
        frozenTypeSet.add(SysConstant.MANUAL_FREEZING);
        frozenTypeSet.add(SysConstant.WAITING_FREEZING);
        frozenTypeSet.add(SysConstant.WAITING_FREEZING_FROZEN);
        frozenTypeSet.add(SysConstant.UNFREEZE);
    }
}
