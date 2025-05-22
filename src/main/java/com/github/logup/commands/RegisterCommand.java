package com.github.logup.commands;

import com.github.logup.Main;
import com.github.logup.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {
    private final Main plugin;

    public RegisterCommand(Main plugin) {
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

        if (plugin.getAuthManager().isRegistered(player)) {
            player.sendMessage(ChatColor.RED + config.getMessage("already-registered"));
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + config.getMessage("register-usage"));
            return true;
        }

        String password = args[0];
        String confirmPassword = args[1];

        if (!password.equals(confirmPassword)) {
            player.sendMessage(ChatColor.RED + config.getMessage("password-mismatch"));
            return true;
        }

        if (password.length() < config.getMinPasswordLength()) {
            player.sendMessage(ChatColor.RED + config.getMessage("password-too-short").replace("%min%", String.valueOf(config.getMinPasswordLength())));
            return true;
        }

        if (password.length() > config.getMaxPasswordLength()) {
            player.sendMessage(ChatColor.RED + config.getMessage("password-too-long").replace("%max%", String.valueOf(config.getMaxPasswordLength())));
            return true;
        }

        try {
            plugin.getAuthManager().registerPlayer(player, password);
            player.sendMessage(ChatColor.GREEN + config.getMessage("register-success"));
        } catch (Exception e) {
            plugin.getLogger().severe("Error registering player " + player.getName() + ": " + e.getMessage());
            player.sendMessage(ChatColor.RED + config.getMessage("error"));
        }
        return true;
    }
}