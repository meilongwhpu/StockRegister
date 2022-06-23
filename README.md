# StockRegister

StockRegister是基于FISCO BCOS区块链底层平台研发的股权登记系统，该系统提供了合约治理、账户管理、企业管理、股权管理四大类功能。合约治理主要功能包括创建企业信息合约、创建企业股权合约、创建账户、合约的状态及归属者管理等；账户管理主要功能包括内外部账户映射管理、添加及查询账户持股合约等；企业管理主要功能包括企业信息管理、董监高信息管理、查询企业信息变更历史记录等；股权管理主要包括股份发行、股份增资及、股份减资、股权登记、冻结登记、质押登记、交易登记、历史记录以及各类数据的查询等。


## 关键特性
- 灵活的账户体系

  为投资者建立合约账户，将区块链外部账户与合约账户地址进行关联，支持对合约账户的冻结、解冻、销户等操作，由合约账户关联其他业务。

- 基于Linked Event的存储结构

  将交易变动通过Event存入每个区块都有对应的Event存储区，通过遍历区块的Event事件即可得到所有的历史变动记录。
    
- 基于工厂模式的合约设计

  Governor合约创建和部署其他业务类合约并将业务类合约地址存入Governor合约中，Web应用层无需关注业务类合约的合约地址，将合约地址数据存在链上，保证了数据的安全性。

- 基于分层思路的合约设计

  业务合约与数据合约解耦。若业务逻辑发生变化而需升级业务合约时无需业务数据的迁移。同时为了保障数据合约的安全性，对数据合约的访问权限设置了严格控制。

## 环境要求

在使用本项目前，请确认系统环境已安装相关依赖软件，清单如下：

| 依赖软件   | 说明                                                         | 备注 |
| ---------- | ------------------------------------------------------------ | ---- |
| FISCO-BCOS       | >= 2.7.2 |      |
| Java       | \>= JDK[1.8]                                                 |      |
| Git        | 下载的安装包使用Git                                          |      |

## 运行
## 1. 证书拷贝

将节点所在目录`nodes/${ip}/sdk`下的ca.crt、sdk.crt和sdk.key文件拷贝到项目的`src/main/resources/conf`目录下供SDK使用


## 2. 配置连接节点

请修改src/main/resources/application.properties，该文件包含如下信息：
```
### Java sdk configuration
cryptoMaterial.certPath=conf
network.peers[0]=127.0.0.1:20200
network.peers[1]=127.0.0.1:20201

### System configuration
system.groupId=1

### Springboot configuration
server.port=8080

```
其中：
- java sdk configuration部分配置如下：
    * 请将network.peers更换成实际的链节点监听地址。
    * cryptoMaterial.certPath设为conf

- System configuration配置部分，需要配置：
    * system.groupId设为目标群组，默认为1
    * 
## 3. 配置智能合约地址

请修改src/main/resources/application.properties，该文件包含如下信息：
```
### AccountManager合约地址
smart.contract.accountManager.address=0x31406a7cd73ab05220add308fa8f91b7bcc60913
### Governor合约地址
smart.contract.governor.address=0xd1b1bcbbaef6e53e7b0c4a711e5c9dad27374881

### 部署合约的账户
deploy.contract.account=0x14e8e34f9b993f01e87cbdeed3a6b76237e6c1a4

```
其中：
- smart.contract.accountManager.address为AccountManager合约地址，需提前在区块链上部署此合约。

- smart.contract.governor.address为Governor合约地址，需提前在区块链上部署此合约。
- deploy.contract.account为部署合约的账户地址。

## 4. 编译和运行
您可以在idea内直接运行，也可以编译成可执行jar包后在Linux系统中运行。利用gradle编译jar包以及配置文件存入dist目录下，生成stockregister-1.0.1-exec.jar，可执行此jar包：
```
java -jar stockregister-1.0.1-exec.jar
```
随后，即可访问相关接口。
