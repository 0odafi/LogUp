package com.github.logup.listeners;

import com.github.logup.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {
    private final Main plugin;

    public MovementListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.getAuthManager().isNotLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}