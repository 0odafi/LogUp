package com.github.logup;

import com.github.logup.auth.AuthManager;
import com.github.logup.commands.LoginCommand;
import com.github.logup.commands.RegisterCommand;
import com.github.logup.config.ConfigManager;
import com.github.logup.listeners.ChatListener;
import com.github.logup.listeners.JoinListener;
import com.github.logup.listeners.MovementListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main instance;
    private AuthManager authManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Initialize modules
        configManager = new ConfigManager(this);
        authManager = new AuthManager(getConfig(), this::saveConfig);

        // Register listeners
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new MovementListener(this), this);

        // Register commands
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("login").setExecutor(new LoginCommand(this));

        getLogger().info("LogUpPlugin successfully enabled!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        getLogger().info("LogUpPlugin disabled.");
    }

    public static Main getInstance() {
        return instance;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}