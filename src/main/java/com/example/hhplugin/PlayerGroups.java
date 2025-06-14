package com.example.hhplugin;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerGroups {
    private final Path filePath;
    private ConfigurationNode root;

    public PlayerGroups(Path dataFolder) throws IOException {
        filePath = dataFolder.resolve("player_groups.yml");
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("player_groups.yml")) {
                Files.copy(in, filePath);
            }
        }
        root = YamlConfigurationLoader.builder().path(filePath).build().load();
    }

    public void reload() throws IOException {
        if (!filePath.toFile().exists()) {
            filePath.toFile().createNewFile();
        }
        root = YamlConfigurationLoader.builder().path(filePath).build().load();
    }

    public Set<UUID> getGroup(String group) {
        Set<UUID> set = new HashSet<>();
        for (ConfigurationNode node : root.node(group).childrenList()) {
            String uuidStr = node.getString();
            if (uuidStr != null) set.add(UUID.fromString(uuidStr));
        }
        return set;
    }

    public boolean isOp(UUID uuid) {
        return getGroup("op").contains(uuid);
    }

    public boolean isVip(UUID uuid) {
        return getGroup("vip").contains(uuid);
    }

    public void addOp(UUID uuid) throws IOException {
        Set<UUID> ops = getGroup("op");
        ops.add(uuid);
        saveGroup("op", ops);
    }

    public void addVip(UUID uuid) throws IOException {
        Set<UUID> vips = getGroup("vip");
        vips.add(uuid);
        saveGroup("vip", vips);
    }

    private void saveGroup(String group, Set<UUID> uuids) throws IOException {
        root.node(group).setList(UUID.class, uuids.stream().toList());
        YamlConfigurationLoader.builder().path(filePath).build().save(root);
    }
}