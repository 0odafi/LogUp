package com.github.logup.config;

import com.github.logup.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final Main plugin;
    private final FileConfiguration config;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public String getMessage(String key) {
        String message = config.getString("messages." + key, "Сообщение не найдено!");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public int getMinPasswordLength() {
        return config.getInt("settings.min-password-length", 6);
    }

    public int getMaxPasswordLength() {
        return config.getInt("settings.max-password-length", 20);
    }
}