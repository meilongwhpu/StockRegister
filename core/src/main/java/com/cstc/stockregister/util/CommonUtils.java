package com.cstc.stockregister.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {

    /**
     * 支持数字，字母与下划线"_"
     *
     * @param input
     * @return
     */
    public static boolean isLetterDigit(String input) {
        String regex = "^[a-z0-9A-Z_]+$";
        return input.matches(regex);
    }

}
