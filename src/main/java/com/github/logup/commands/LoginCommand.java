package com.github.logup.commands;

import com.github.logup.Main;
import com.github.logup.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {
    private final Main plugin;

    public LoginCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only for players!");
            return true;
        }
        Player player = (Player) sender;
        ConfigManager config = plugin.getConfigManager();

        if (!plugin.getAuthManager().isRegistered(player)) {
            player.sendMessage(ChatColor.RED + config.getMessage("not-registered"));
            return true;
        }

        if (!plugin.getAuthManager().isNotLoggedIn(player)) {
            player.sendMessage(ChatColor.RED + config.getMessage("already-logged-in"));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + config.getMessage("login-usage"));
            return true;
        }

        String password = args[0];

        try {
            if (plugin.getAuthManager().loginPlayer(player, password)) {
                player.sendMessage(ChatColor.GREEN + config.getMessage("login-success"));
            } else {
                player.sendMessage(ChatColor.RED + config.getMessage("login-failed"));
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Error logging in player " + player.getName() + ": " + e.getMessage());
            player.sendMessage(ChatColor.RED + config.getMessage("error"));
        }
        return true;
    }
}