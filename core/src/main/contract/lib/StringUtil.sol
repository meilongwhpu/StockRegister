pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Table.sol";

/*
* 处理字符串的工具合约
*
*/
library StringUtil {

   /*
    * 得到表中指定一条记录的值
    *
    * @param fields  各字段名
    * @param fields  记录的实例
    *
    * @return        各字段值
    */
    function getEntry(string[] memory fields, Entry entry) internal view returns (string[] memory) {
        string[] memory values = new string[](fields.length);
        for (uint i = 0; i < fields.length; i++) {
            values[i] = entry.getString(fields[i]);
        }
        return values;
    }



   /*
    * 得到表中指定一条记录的值，以Json字符串格式输出
    *
    * @param fields      各字段名
    * @param primaryKey  主键值
    * @param entries     多条记录的实例
    *
    * @return            执行状态码
    * @return            记录值
    */
    function getJsonString(string primaryKey,string[] memory fields,string primaryValue, Entries entries) internal view returns (uint, string memory) {
        string memory detail;
        if (0 == entries.size()) {
            return (uint(0), detail);
        }
        else {
            detail = "[";
            for (uint i = 0; i < uint(entries.size()); i++) {
                string[] memory values = getEntry(fields, entries.get(int(i)));
                for (uint j = 0; j < values.length; j++) {
                    if (j == 0) {
                        detail = strConcat6(detail, "{\"", primaryKey,"\":\"", primaryValue, "\",");

                        //detail = strConcat4(detail, "{\"primaryKey\":\"", primaryValue, "\",fields\":{");
                    }
                    detail = strConcat6(detail, "\"", fields[j], "\":\"", values[j], "\"");

                    if (j == values.length - 1) {
                        detail = strConcat2(detail, "}");
                    } else {
                        detail = strConcat2(detail, ",");
                    }
                }
                if (i != uint(entries.size()) - 1) {
                    detail = strConcat2(detail, ",");
                }
            }
            detail = strConcat2(detail, "]");
            return (uint(1), detail);
        }
    }

    function strConcat2(string memory str1, string memory str2) internal pure returns (string memory) {
        string[] memory strings = new string[](2);
        strings[0] = str1;
        strings[1] = str2;
        return strConcat(strings);
    }

    function strConcat4(
        string memory str1,
        string memory str2,
        string memory str3,
        string memory str4
    ) internal pure returns (string memory) {
        string[] memory strings = new string[](4);
        strings[0] = str1;
        strings[1] = str2;
        strings[2] = str3;
        strings[3] = str4;
        return strConcat(strings);
    }

    function strConcat6(
        string memory str1,
        string memory str2,
        string memory str3,
        string memory str4,
        string memory str5,
        string memory str6
    ) internal pure returns (string memory) {
        string[] memory strings = new string[](6);
        strings[0] = str1;
        strings[1] = str2;
        strings[2] = str3;
        strings[3] = str4;
        strings[4] = str5;
        strings[5] = str6;
        return strConcat(strings);
    }

    function strConcat(string[] memory strings) internal pure returns (string memory) {
        // 计算字节长度
        uint bLength = 0;
        for (uint i = 0; i < strings.length; i++) {
            bLength += bytes(strings[i]).length;
        }

        // 实例化字符串
        string memory result = new string(bLength);
        bytes memory bResult = bytes(result);

        // 填充字符串
        uint currLength = 0;
        for ( i = 0; i < strings.length; i++) {
            // 将当前字符串转换为字节数组
            bytes memory bs = bytes(strings[i]);
            for (uint j = 0; j < bs.length; j++) {
                bResult[currLength] = bs[j];
                currLength++;
            }
        }
        return string(bResult);
    }

}
