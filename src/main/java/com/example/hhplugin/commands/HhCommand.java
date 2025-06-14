package com.example.hhplugin.commands;
import com.example.hhplugin.HhPlugin;
import com.example.hhplugin.Config;
import com.example.hhplugin.PlayerGroups;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.SimpleCommand.Invocation;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HhCommand implements SimpleCommand {
    private final HhPlugin plugin;
    private final Logger logger;
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    public HhCommand(HhPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    public void register() {
        for (String alias : plugin.getConfig().aliases()) {//注册别名
            plugin.getProxy().getCommandManager().register(
                plugin.getProxy().getCommandManager().metaBuilder(alias).build(),
                this
            );
        }
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player)) {
            source.sendMessage(Component.text("只有玩家可以使用此命令！"));
            return;
        }

        Player player = (Player) source;
        UUID playerId = player.getUniqueId();
        Config config = plugin.getConfig();
        PlayerGroups groups = plugin.getPlayerGroups();
        // 冷却检查
        long currentTime = System.currentTimeMillis();
        int cooldownSec = config.cd();
        if (groups.isOp(playerId)) {
            cooldownSec = config.opcd();
        } else if (groups.isVip(playerId) && config.vipmode()) {//判断是否开启vip模式
            cooldownSec = config.vipcd();
        }
        if (cooldowns.containsKey(playerId)) {
            long elapsed = currentTime - cooldowns.get(playerId);
            if (elapsed < cooldownSec * 1000L) {
                long remaining = (cooldownSec * 1000L - elapsed) / 1000;
                Component msg = MiniMessage.miniMessage().deserialize(
                    config.coolmsg().replace("%time%", String.valueOf(remaining))
                );
                player.sendMessage(msg);
                return;
            }
        }

        // 参数检查
        if (args.length == 0) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                config.errmsg()
            ));
            return;
        }

        // 更新冷却时间
        cooldowns.put(playerId, currentTime);

        // 构建消息
        String messageContent = String.join(" ", args);
        RegisteredServer currentServer = player.getCurrentServer()
            .orElseThrow(() -> new IllegalStateException("玩家不在任何服务器中"))
            .getServer();

        String formattedMessage = config.text()
            .replace("%server%", currentServer.getServerInfo().getName())
            .replace("%player%", player.getUsername())
            .replace("%message%", messageContent);

        String joinText = config.jointext();
        formattedMessage = formattedMessage + " <click:run_command:'/server " + currentServer.getServerInfo().getName() + "'>" + joinText + "</click>";
        // 记录日志
        logger.info("玩家 {} 在服务器 {} 发送了喊话：{}",
            player.getUsername(),
            currentServer.getServerInfo().getName(),
            messageContent);
        // 全服广播
        Component broadcastMsg = MiniMessage.miniMessage().deserialize(formattedMessage);
        plugin.getProxy().getAllPlayers().forEach(p -> p.sendMessage(broadcastMsg));
    }
}