package com.broja.novaannouncer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final NovaAnnouncerPlugin plugin;

    public JoinListener(NovaAnnouncerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        event.setJoinMessage("§a[+] §rWelcome " + playerName + " to the server!");
        plugin.getLogger().info(playerName + " joined.");
    }
}
