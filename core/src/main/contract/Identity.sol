pragma solidity ^0.4.24;
pragma experimental ABIEncoderV2;

import "./storage/BytesMapLib.sol";

contract Identity
{
    using BytesMapLib for BytesMapLib.Map;

    //合约状态： 0-normal, 1-stop
    uint8 _contractStatus = 0;

    //治理合约地址
    address governor;
    //合约拥着者
    address owner;
    //创建合约的账户地址
    address creator;
    BytesMapLib.Map _storage;

    modifier onlyCreatorOrGovernor() {
        require(msg.sender == address(governor)||tx.origin == address(creator), "only governor or creator is authorized.");
        _;
    }

    modifier onlyCreator() {
        require(_contractStatus == 0, " only contract status is normal.");
        require(msg.sender == address(creator) ||tx.origin == address(creator), "only creator is authorized.");
        _;
    }

    modifier onlyGovernor() {
        require(_contractStatus == 0, " only contract status is normal.");
        require(msg.sender == address(governor), "only governor is authorized.");
        _;
    }

    constructor(address _governor,address _creator,address _owner){
        governor = _governor;
        creator = _creator;
        owner=_owner;
    }

    function stopContract() public returns(bool){
        require(msg.sender == address(governor), "only governor is authorized.");
        _contractStatus=1;
        return true;
    }
    function restartContract() public returns(bool){
        require(msg.sender == address(governor), "only governor is authorized.");
        _contractStatus=0;
        return true;
    }



    function getGovernor() public view returns (address) {
        return governor;
    }

    function getCreator() public view returns (address) {
        return creator;
    }

    function getOwner() public view returns (address) {
        return owner;
    }

    function setOwner(address _owner) public onlyGovernor  {
        owner=_owner;
    }

    function setGovernor(address _governor) public onlyGovernor  {
        governor=_governor;
    }

    function setCreator(address _creator) public onlyGovernor {
        creator=_creator;
    }

    function getValue(bytes key) public view returns (bytes)
    {
        return _storage.get(key);
    }

    //更新key值的value
    function setKeyValue(bytes key, bytes value) public onlyCreatorOrGovernor returns (bool replaced)
    {
        return _storage.set(key, value);
    }

    //无key值则新增，有key值则更新
    function insertKeyValue(bytes key, bytes value) onlyCreatorOrGovernor returns (bool replaced)
    {
        return _storage.insert(key, value);
    }

    function removeValue(bytes key) onlyCreatorOrGovernor returns (bool replaced)
    {
        return _storage.remove(key);
    }

    function sizeOfStorage() public view returns (uint)
    {
        return _storage.size();
    }

}