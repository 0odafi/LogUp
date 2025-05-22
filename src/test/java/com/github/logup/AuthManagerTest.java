package com.github.logup;

import com.github.logup.auth.AuthManager;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthManagerTest {
    private AuthManager authManager;
    private MemoryConfiguration config;
    private Player player;
    private JavaPlugin plugin;

    @BeforeEach
    void setUp() {
        // Создаём реальную MemoryConfiguration
        config = new MemoryConfiguration();
        // Мокаем игрока
        player = mock(Player.class);
        when(player.getName()).thenReturn("TestPlayer");
        // Мокаем JavaPlugin
        plugin = mock(JavaPlugin.class);
        // Инициализируем AuthManager с двумя аргументами
        authManager = new AuthManager(plugin, config);
    }

    @Test
    void testRegisterPlayer() {
        // Регистрируем игрока
        authManager.registerPlayer(player, "password123");

        // Проверяем, что пароль сохранён в конфигурации
        String storedPassword = config.getString("players.TestPlayer.password");
        assertNotNull(storedPassword);
        // Проверяем, что пароль хеширован
        assertTrue(authManager.checkPassword("password123", storedPassword));
    }

    @Test
    void testLoginSuccess() {
        // Устанавливаем хешированный пароль в конфигурацию
        String hashedPassword = authManager.hashPassword("password123");
        config.set("players.TestPlayer.password", hashedPassword);

        // Выполняем логин
        authManager.loginPlayer(player, "password123");

        // Проверяем, что игрок получил сообщение об успешном логине
        verify(player, times(1)).sendMessage("Вы успешно вошли!");
    }

    @Test
    void testLoginFailure() {
        // Устанавливаем хешированный пароль в конфигурацию
        String hashedPassword = authManager.hashPassword("password123");
        config.set("players.TestPlayer.password", hashedPassword);

        // Выполняем логин с неверным паролем
        authManager.loginPlayer(player, "wrongPassword");

        // Проверяем, что игрок получил сообщение об ошибке
        verify(player, times(1)).sendMessage("Неверный пароль!");
    }
}