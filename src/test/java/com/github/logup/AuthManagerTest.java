package com.github.logup;

import com.github.logup.auth.AuthManager;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthManagerTest {
    private AuthManager authManager;
    private MemoryConfiguration config;
    private Player player;

    @BeforeEach
    void setUp() {
        // Создаём реальную MemoryConfiguration вместо мокинга
        config = new MemoryConfiguration();
        // Мокаем игрока
        player = Mockito.mock(Player.class);
        when(player.getName()).thenReturn("TestPlayer");
        // Инициализируем AuthManager
        authManager = new AuthManager(config);
    }

    @Test
    void testRegisterPlayer() {
        // Настраиваем конфигурацию
        config.set("players.TestPlayer.password", null);

        // Регистрируем игрока
        boolean result = authManager.registerPlayer(player, "password123");

        // Проверяем результат
        assertTrue(result);
        assertNotNull(config.getString("players.TestPlayer.password"));
    }

    @Test
    void testLoginSuccess() {
        // Настраиваем конфигурацию с хешированным паролем
        String hashedPassword = authManager.hashPassword("password123");
        config.set("players.TestPlayer.password", hashedPassword);

        // Проверяем успешный логин
        boolean result = authManager.loginPlayer(player, "password123");

        assertTrue(result);
    }

    @Test
    void testLoginFailure() {
        // Настраиваем конфигурацию с хешированным паролем
        String hashedPassword = authManager.hashPassword("password123");
        config.set("players.TestPlayer.password", hashedPassword);

        // Проверяем неуспешный логин с неверным паролем
        boolean result = authManager.loginPlayer(player, "wrongPassword");

        assertFalse(result);
    }
}