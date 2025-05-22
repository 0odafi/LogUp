package com.github.logup.auth;

import com.github.logup.Main;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AuthManager {
    private final Main plugin;
    private final Set<UUID> loggedInPlayers = new HashSet<>();
    private final Set<UUID> notLoggedInPlayers = new HashSet<>();
    private final Map<UUID, String> pendingPasswords = new HashMap<>();

    public AuthManager(Main plugin) {
        this.plugin = plugin;
    }

    public void registerPlayer(Player player, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        plugin.getConfig().set("players." + player.getUniqueId().toString() + ".password", hashedPassword);
        plugin.saveConfig();
        addPlayer(player);
    }

    public boolean loginPlayer(Player player, String password) {
        String storedHash = plugin.getConfig().getString("players." + player.getUniqueId().toString() + ".password");
        if (storedHash == null) {
            return false;
        }
        if (BCrypt.checkpw(password, storedHash)) {
            addPlayer(player);
            return true;
        }
        return false;
    }

    public boolean isRegistered(Player player) {
        return plugin.getConfig().contains("players." + player.getUniqueId().toString() + ".password");
    }

    public void addNotLoggedInPlayer(Player player) {
        notLoggedInPlayers.add(player.getUniqueId());
    }

    public boolean isNotLoggedIn(Player player) {
        return notLoggedInPlayers.contains(player.getUniqueId());
    }

    public void addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (!loggedInPlayers.contains(uuid)) {
            loggedInPlayers.add(uuid);
            notLoggedInPlayers.remove(uuid);
            pendingPasswords.remove(uuid);
        }
    }

    public void storePendingPassword(Player player, String password) {
        pendingPasswords.put(player.getUniqueId(), password);
    }

    public boolean confirmPendingPassword(Player player, String password) {
        String storedPassword = pendingPasswords.get(player.getUniqueId());
        if (storedPassword == null) {
            return false;
        }
        return storedPassword.equals(password);
    }

    public void removePendingPassword(Player player) {
        pendingPasswords.remove(player.getUniqueId());
    }
}