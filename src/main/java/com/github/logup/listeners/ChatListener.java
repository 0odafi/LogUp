package com.github.logup.listeners;

import com.github.logup.Main;
import com.github.logup.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatListener implements Listener {
    private final Main plugin;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ConfigManager config = plugin.getConfigManager();

        if (plugin.getAuthManager().isNotLoggedIn(player) && plugin.getAuthManager().isRegistered(player)) {
            event.setCancelled(true);
            String message = event.getMessage();
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        if (plugin.getAuthManager().loginPlayer(player, message)) {
                            player.sendMessage(ChatColor.GREEN + config.getMessage("login-success"));
                        } else {
                            player.sendMessage(ChatColor.RED + config.getMessage("login-failed"));
                        }
                    } catch (Exception e) {
                        plugin.getLogger().severe("Ошибка при авторизации через чат для " + player.getName() + ": " + e.getMessage());
                        player.sendMessage(ChatColor.RED + config.getMessage("error"));
                    }
                }
            }.runTask(plugin);
        }
    }
}