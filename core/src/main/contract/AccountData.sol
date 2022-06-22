pragma solidity ^0.4.24;
pragma experimental ABIEncoderV2;

import "./Identity.sol";
import "./storage/Bytes2AddressMapLib.sol";
contract AccountData is Identity{

    using Bytes2AddressMapLib for Bytes2AddressMapLib.Map;

    Bytes2AddressMapLib.Map stockAssetMap;
    Bytes2AddressMapLib.Map bondAssetMap;

    function AccountData(address _creator,address _owner) Identity(msg.sender,_creator,_owner) public {
    }

    function addStockAsset(bytes stockAssetCode,address assetAddress) public onlyCreatorOrGovernor  returns (bool) {
        return stockAssetMap.add(stockAssetCode,assetAddress);
    }

    function addBondAsset(bytes bondAssetCode,address assetAddress) public onlyCreatorOrGovernor  returns (bool) {
        return bondAssetMap.add(bondAssetCode,assetAddress);
    }

    function getStockAssets(bytes stockAssetCode) public view returns (address) {
        return stockAssetMap.get(stockAssetCode);
    }

    function getBondAssets(bytes bondAssetCode) public view returns (address) {
        return bondAssetMap.get(bondAssetCode);
    }

}