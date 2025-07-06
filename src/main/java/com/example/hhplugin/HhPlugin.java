package com.example.hhplugin;

import com.example.hhplugin.commands.HhCommand;
import com.example.hhplugin.Config;
import com.example.hhplugin.PlayerGroups;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
    id = "hhplugin",
    name = "HH Plugin",
    version = "1.3.1",
    authors = {"Dicksuck","QQCoin"}
)
public class HhPlugin {
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;
    private Config config;
    private PlayerGroups playerGroups;

    @Inject
    public HhPlugin(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    public Logger getLogger() {
        return this.logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            config = new Config(dataDirectory);
        } catch (IOException e) {
            logger.error("配置文件加载失败", e);
        }
        try {
            playerGroups = new PlayerGroups(dataDirectory);
        } catch (IOException e) {
            logger.error("玩家组文件加载失败", e);
        }
        new HhCommand(this).register();
        new com.example.hhplugin.commands.OPCommand(this).register();
        logger.info(" __    __   __    __  .______    __        __    __    ______   __  .__   __. ");
        logger.info("|  |  |  | |  |  |  | |   _  \\  |  |      |  |  |  |  /  ____| |  | |  \\ |  | ");
        logger.info("|  |__|  | |  |__|  | |  |_)  | |  |      |  |  |  | |  |  __  |  | |   \\|  | ");
        logger.info("|   __   | |   __   | |   ___/  |  |      |  |  |  | |  | |_ | |  | |  . `  | ");
        logger.info("|  |  |  | |  |  |  | |  |      |  |____  |  |__|  | |  |__| | |  | |  |\\   | ");
        logger.info("|__|  |__| |__|  |__| |__|      |_______| |________| |_______| |__| |__| \\__|");
        logger.info("         HH Plugin 已加载!");
    }
    public ProxyServer getProxy() {
        return proxy;
    }
    public Config getConfig() {
        return config;
    }
    public PlayerGroups getPlayerGroups() {
        return playerGroups;
    }
}