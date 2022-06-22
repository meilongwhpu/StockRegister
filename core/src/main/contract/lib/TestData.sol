pragma solidity ^0.4.24;
pragma experimental ABIEncoderV2;

import "./BytesMapLib.sol";
import "./BytesSetLib.sol";
contract TestData {

    using BytesMapLib for BytesMapLib.Map;
    using BytesSetLib for BytesSetLib.Set;


    BytesMapLib.Map test;
    BytesSetLib.Set testSet;

    bytes[] attrValues;

    function setValue(bytes key,bytes value)public returns(bool){
        return test.add(key,value);
    }
    function getValue(bytes key)public returns(bytes,uint){
        return (test.get(key),test.get(key).length);
    }

    function TestView(uint instr) view returns(bool){
        require(1==instr,"erro222");
        return true;
    }


    function setValueArrays(bytes[] orgInfoStr)public returns(bool){
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
        return true;
    }

    function updateAttrValues(bytes[] key)public  returns(bool){
        for(uint i=0;i<key.length;i++){
            attrValues[i]=key[i];
        }
        return true;
    }

    function getAttrValues()public view returns(bytes[]){
        return attrValues;
    }


    function setValueSet(bytes key)public returns(bool){
        return testSet.insert(key);
    }
    function getAllValueSet()public returns(bytes[]){
        return testSet.getAll();
    }

}