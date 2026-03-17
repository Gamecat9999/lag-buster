package com.broja.novaannouncer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class NovaAnnouncerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        getLogger().info("NovaAnnouncer enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("NovaAnnouncer disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("shout".equalsIgnoreCase(command.getName())) {
            if (args.length == 0) {
                sender.sendMessage("§cUsage: /shout <message>");
                return true;
            }

            String message = String.join(" ", args);
            Bukkit.broadcastMessage("§6[Shout] §r" + sender.getName() + ": §f" + message);
            return true;
        }
        return false;
    }
}
