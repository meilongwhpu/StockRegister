package com.cstc.stockregister.service;

import java.util.List;

public interface TestDataService {
    public boolean TestView(int input)throws Exception;
    public boolean addValue(String key,String value) throws  Exception;

    public String getValue(String key) throws  Exception;

    public boolean insertValueSet(String key) throws Exception;

    public String getAllValueSet() throws  Exception;

    public boolean setValueArrays(List<String> key) throws Exception;

    public boolean updateValueArrays(List<String> key) throws Exception;

    public String getValueArrays() throws  Exception;
}
