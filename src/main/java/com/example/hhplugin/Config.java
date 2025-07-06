package com.example.hhplugin;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Config {
    private final ConfigurationNode root;
    public Config(Path dataFolder) throws IOException {
        Path configPath = dataFolder.resolve("config.yml");
        if (!Files.exists(configPath)) {
            Files.createDirectories(configPath.getParent());
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml")) {
                Files.copy(in, configPath);
            }
        }
        root = YamlConfigurationLoader.builder().path(configPath).build().load();
    }
    public String text() { return root.node("text").getString(); }
    public String jointext() { return root.node("jointext").getString(); }
    public String coolmsg() { return root.node("coolmsg").getString(); }
    public String errmsg() { return root.node("errmsg").getString(); }
    public int cd() { return root.node("cd").getInt(); }
    public int opcd() { return root.node("opcd").getInt(); }
    public int vipcd() { return root.node("vipcd").getInt(); }
    public boolean vipmode() { return root.node("vip").getBoolean(); }
    public List<String> aliases() {
        return root.node("aliases").childrenList().stream()
            .map(n -> n.getString())
            .collect(Collectors.toList());
    }
}