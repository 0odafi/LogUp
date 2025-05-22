package com.github.logup;

import com.github.logup.auth.AuthManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthManagerTest {
    private Main plugin;
    private AuthManager authManager;
    private Player player;

    @BeforeEach
    void setUp() {
        plugin = mock(Main.class);
        authManager = new AuthManager(plugin);
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(java.util.UUID.randomUUID());
    }

    @Test
    void testRegisterPlayer() {
        FileConfiguration config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        authManager.registerPlayer(player, "test123");
        verify(config).set(eq("players." + player.getUniqueId().toString() + ".password"), anyString());
        verify(plugin).saveConfig();
        assertTrue(authManager.isRegistered(player));
    }

    @Test
    void testLoginSuccess() {
        FileConfiguration config = mock(FileConfiguration.class);
        String hashedPassword = BCrypt.hashpw("test123", BCrypt.gensalt());
        when(plugin.getConfig()).thenReturn(config);
        when(config.getString("players." + player.getUniqueId().toString() + ".password")).thenReturn(hashedPassword);
        assertTrue(authManager.loginPlayer(player, "test123"));
        assertFalse(authManager.isNotLoggedIn(player));
    }
}