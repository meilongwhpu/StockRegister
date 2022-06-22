pragma solidity ^0.4.25;

/** 类型转换工具 */
library LibTypeConversion {
    //uint 转str
    function uint2str(uint i) internal pure returns (string c) {
        if (i == 0) return "0";
        uint j = i;
        uint length;
        while (j != 0) {
            length++;
            j /= 10;
        }
        bytes memory bstr = new bytes(length);
        uint k = length - 1;
        while (i != 0) {
            bstr[k--] = byte(48 + i % 10);
            i /= 10;
        }
        c = string(bstr);
    }
    function str2uint(string memory s) internal pure returns(uint) {
        bytes memory b = bytes(s);
        uint result = 0;
        for(uint i = 0; i < b.length; i++) {
            if(uint8(b[i]) >= 48 && uint8(b[i]) <= 57) {
                result = result * 10 + (uint8(b[i]) - 48);
            }
        }
        return result;
    }

    function addressToString(address addr) internal pure returns(string memory){
        //Convert addr to bytes
        bytes20 value = bytes20(uint160(addr));
        bytes memory strBytes = new bytes(42);
        //Encode hex prefix
        strBytes[0] = '0';
        strBytes[1] = 'x';
        //Encode bytes usig hex encoding
        for(uint i=0;i<20;i++){
            uint8 byteValue = uint8(value[i]);
            strBytes[2 + (i<<1)] = encode((byteValue >> 4) & 0x0f);
            strBytes[3 + (i<<1)] = encode(byteValue & 0x0f);
        }
        return string(strBytes);
    }

    function stringToAddress(string memory data) internal pure  returns(address){
        bytes memory strBytes = bytes(data);
        require(strBytes.length >= 39 && strBytes.length <= 42, "Not hex string");
        //Skip prefix
        uint start = 0;
        uint bytesBegin = 0;
        if(strBytes[1] == 'x' || strBytes[1] == 'X'){
            start = 2;
        }
        //Special case: 0xabc. should be 0x0abc
        uint160 addrValue = 0;
        uint effectPayloadLen = strBytes.length - start;
        if(effectPayloadLen == 39){
            addrValue += decode(strBytes[start++]);
            bytesBegin++;
        }
        //Main loop
        for(uint i=bytesBegin;i < 20; i++){
            addrValue <<= 8;
            uint8 tmp1 = decode(strBytes[start]);
            uint8 tmp2 = decode(strBytes[start+1]);
            uint8 combined = (tmp1 << 4) + tmp2;
            addrValue += combined;
            start+=2;
        }

        return address(addrValue);
    }

    //-----------HELPER METHOD--------------//

    //num represents a number from 0-15 and returns ascii representing [0-9A-Fa-f]
    function encode(uint8 num) private pure returns(byte){
        //0-9 -> 0-9
        if(num >= 0 && num <= 9){
            return byte(num + 48);
        }
        //10-15 -> a-f
        return byte(num + 87);
    }

    //asc represents one of the char:[0-9A-Fa-f] and returns consperronding value from 0-15
    function decode(byte asc) private pure returns(uint8){
        uint8 val = uint8(asc);
        //0-9
        if(val >= 48 && val <= 57){
            return val - 48;
        }
        //A-F
        if(val >= 65 && val <= 70){
            return val - 55;
        }
        //a-f
        return val - 87;
    }

}