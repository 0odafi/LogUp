package com.github.logup;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthManagerTest {
    private com.github.logup.auth.AuthManager authManager;
    private MemoryConfiguration config;
    private Player player;
    private Runnable sessionEndCallback;

    @BeforeEach
    void setUp() {
        // Создаём реальную MemoryConfiguration
        config = new MemoryConfiguration();
        // Мокаем игрока
        player = mock(Player.class);
        when(player.getName()).thenReturn("TestPlayer");
        // Мокаем Runnable для sessionEndCallback
        sessionEndCallback = mock(Runnable.class);
        // Инициализируем AuthManager
        authManager = new com.github.logup.auth.AuthManager(config, sessionEndCallback);
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
        // Проверяем, что вызван callback
        verify(sessionEndCallback, times(1)).run();
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
        // Проверяем, что callback не вызван
        verify(sessionEndCallback, never()).run();
    }
}