package com.github.logup.listeners;

import com.github.logup.Main;
import com.github.logup.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ConfigManager config = plugin.getConfigManager();

        if (!plugin.getAuthManager().isRegistered(player)) {
            plugin.getAuthManager().addNotLoggedInPlayer(player);
            player.sendMessage(ChatColor.YELLOW + config.getMessage("register-prompt"));
        } else {
            plugin.getAuthManager().addNotLoggedInPlayer(player);
            player.sendMessage(ChatColor.YELLOW + config.getMessage("login-prompt"));
        }
    }
}