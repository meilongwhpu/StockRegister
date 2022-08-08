package com.cstc.stockregister.configuration;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.AmopTopic;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.model.Message;
import org.fisco.bcos.sdk.model.NodeVersion;
import org.fisco.bcos.sdk.network.MsgHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "sdk")
public class BcosConfig {
    private Map<String, Object> cryptoMaterial;
    public Map<String, List<String>> network;
    public List<AmopTopic> amop;
    public Map<String, Object> account;
    public Map<String, Object> threadPool;

    // add channel disconnect
    public static boolean PEER_CONNECTED = true;
    static class Web3ChannelMsg implements MsgHandler {
        @Override
        public void onConnect(ChannelHandlerContext ctx) {
            PEER_CONNECTED = true;
            log.info("Web3ChannelMsg onConnect:{}, status:{}", ctx.channel().remoteAddress(), PEER_CONNECTED);
        }

        @Override
        public void onMessage(ChannelHandlerContext ctx, Message msg) {
            // not added in message handler, ignore this override
            log.info("Web3ChannelMsg onMessage:{}, status:{}", ctx.channel().remoteAddress(), PEER_CONNECTED);
        }

        @Override
        public void onDisconnect(ChannelHandlerContext ctx) {
            PEER_CONNECTED = false;
            log.error("Web3ChannelMsg onDisconnect:{}, status:{}", ctx.channel().remoteAddress(), PEER_CONNECTED);
        }
    }

    @Bean
    public BcosSDK getBcosSDK() throws ConfigException {
        log.info("start init ConfigProperty");
        // user no need set this:cryptoMaterial.put("sslCryptoType", encryptType);
        log.info("init cert cryptoMaterial:{}, (using conf as cert path)", cryptoMaterial);

        // peers, default one node in front
        Map<String, Object> network = new HashMap<>();
        List<String> peers = this.getNetwork().get("peers");
        network.put("peers", peers);
        log.info("init node network property :{}", peers);

        // init property
        ConfigProperty configProperty = new ConfigProperty();
        configProperty.setCryptoMaterial(cryptoMaterial);
        configProperty.setNetwork(network);
        // init config option
        log.info("init configOption from configProperty");
        ConfigOption configOption = new ConfigOption(configProperty);
        // init bcosSDK
        log.info("init bcos sdk instance, please check sdk.log");
        BcosSDK bcosSDK = new BcosSDK(configOption);

        log.info("init client version");
        NodeVersion.ClientVersion version = bcosSDK.getGroupManagerService().getNodeVersion(peers.get(0))
                .getNodeVersion();
        log.info("init client version,version={},chainId={}",version.getVersion(),version.getChainId());

        Web3ChannelMsg disconnectMsg = new Web3ChannelMsg();
        bcosSDK.getChannel().addConnectHandler(disconnectMsg);
        bcosSDK.getChannel().addDisconnectHandler(disconnectMsg);
        return bcosSDK;
    }

}
