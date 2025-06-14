package com.example.hhplugin.commands;

import com.example.hhplugin.HhPlugin;
import com.example.hhplugin.PlayerGroups;
import com.example.hhplugin.Config;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class OPCommand implements SimpleCommand {
    private final HhPlugin plugin;
    private final Logger logger;

    public OPCommand(HhPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void register() {
        ProxyServer proxy = plugin.getProxy();
        proxy.getCommandManager().register(
            proxy.getCommandManager().metaBuilder("hhop").build(), this
        );
        proxy.getCommandManager().register(
            proxy.getCommandManager().metaBuilder("hhvip").build(), this
        );
        proxy.getCommandManager().register(
            proxy.getCommandManager().metaBuilder("hhreload").build(), this
        );
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String alias = invocation.alias();
        String[] args = invocation.arguments();

        // 权限判断：控制台或OP
        boolean isConsole = !(source instanceof Player);
        boolean isOp = false;
        UUID senderId = null;
        if (source instanceof Player player) {
            senderId = player.getUniqueId();
            isOp = plugin.getPlayerGroups().isOp(senderId);
        }
        if (!isConsole && !isOp) {
            source.sendMessage(Component.text("你没有权限使用此命令！"));
            return;
        }

        try {
            switch (alias.toLowerCase()) {
                case "hhop" -> {
                    if (args.length != 1) {
                        source.sendMessage(Component.text("用法: /hhop <玩家名>"));
                        return;
                    }
                    Optional<Player> targetOpt = plugin.getProxy().getPlayer(args[0]);
                    if (targetOpt.isEmpty()) {
                        source.sendMessage(Component.text("未找到该玩家！"));
                        return;
                    }
                    Player target = targetOpt.get();
                    plugin.getPlayerGroups().addOp(target.getUniqueId());
                    plugin.getPlayerGroups().reload();
                    source.sendMessage(Component.text("已将 " + target.getUsername() + " 添加为OP！"));
                }
                case "hhvip" -> {
                    if (args.length != 1) {
                        source.sendMessage(Component.text("用法: /hhvip <玩家名>"));
                        return;
                    }
                    Optional<Player> targetOpt = plugin.getProxy().getPlayer(args[0]);
                    if (targetOpt.isEmpty()) {
                        source.sendMessage(Component.text("未找到该玩家！"));
                        return;
                    }
                    Player target = targetOpt.get();
                    plugin.getPlayerGroups().addVip(target.getUniqueId());
                    plugin.getPlayerGroups().reload();
                    source.sendMessage(Component.text("已将 " + target.getUsername() + " 添加为VIP！"));
                }
                case "hhreload" -> {
                    plugin.getConfig().getClass(); // 触发NPE检查
                    plugin.getPlayerGroups().reload();
                    source.sendMessage(Component.text("插件配置和权限组已重载！"));
                }
                default -> source.sendMessage(Component.text("未知命令！"));
            }
        } catch (IOException e) {
            logger.error("操作失败", e);
            source.sendMessage(Component.text("操作失败：" + e.getMessage()));
        }
    }
}