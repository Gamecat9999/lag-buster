package com.broja.novaannouncer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Set;

public final class LagBusterPlugin extends JavaPlugin {
    private int clearIntervalTicks;
    private int maxDroppedItems;
    private boolean clearMobsWithAll;
    private boolean debug;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadSettings();

        if (clearIntervalTicks > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    autoCleanup();
                }
            }.runTaskTimer(this, clearIntervalTicks, clearIntervalTicks);
        }

        getLogger().info("LagBuster enabled. Auto-clear interval: " + clearIntervalTicks + " ticks.");
    }

    @Override
    public void onDisable() {
        getLogger().info("LagBuster disabled.");
    }

    private void reloadSettings() {
        reloadConfig();
        clearIntervalTicks = getConfig().getInt("clearIntervalTicks", 6000);
        maxDroppedItems = getConfig().getInt("maxDroppedItems", 120);
        clearMobsWithAll = getConfig().getBoolean("clearMobsWithAll", true);
        debug = getConfig().getBoolean("debug", false);
    }

    private void autoCleanup() {
        int dropped = countEntities(EntityType.DROPPED_ITEM);

        if (dropped >= maxDroppedItems) {
            int removed = cleanupEntities(EntityType.DROPPED_ITEM);
            getLogger().warning("[LagBuster] High dropped item count detected (" + dropped + ") - cleared " + removed + " items.");
        } else if (debug) {
            getLogger().info("[LagBuster] Auto-check: " + dropped + " dropped items (threshold " + maxDroppedItems + ").");
        }
    }

    private int countEntities(EntityType type) {
        int count = 0;
        for (var world : Bukkit.getWorlds()) {
            for (var entity : world.getEntities()) {
                if (entity.getType() == type) {
                    count++;
                }
            }
        }
        return count;
    }

    private int cleanupEntities(EntityType type) {
        int removed = 0;
        for (var world : Bukkit.getWorlds()) {
            for (var entity : world.getEntities()) {
                if (entity.getType() == type) {
                    entity.remove();
                    removed++;
                }
            }
        }
        return removed;
    }

    private int cleanupMobs() {
        Set<EntityType> toClear = EnumSet.of(
                EntityType.COW,
                EntityType.SHEEP,
                EntityType.PIG,
                EntityType.CHICKEN,
                EntityType.HORSE,
                EntityType.VILLAGER
        );

        int removed = 0;
        for (var world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (toClear.contains(entity.getType())) {
                    entity.remove();
                    removed++;
                }
            }
        }
        return removed;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("lagclear".equalsIgnoreCase(command.getName())) {
            if (!sender.hasPermission("lagbuster.clear")) {
                sender.sendMessage("§cYou do not have permission to use this command.");
                return true;
            }

            String type = args.length > 0 ? args[0].toLowerCase() : "drops";
            switch (type) {
                case "drops" -> {
                    int removed = cleanupEntities(EntityType.DROPPED_ITEM);
                    sender.sendMessage("§aLagBuster cleared " + removed + " dropped items.");
                }
                case "mobs" -> {
                    int removed = cleanupMobs();
                    sender.sendMessage("§aLagBuster cleared " + removed + " passive mobs.");
                }
                case "all" -> {
                    int removedDrops = cleanupEntities(EntityType.DROPPED_ITEM);
                    int removedMobs = clearMobsWithAll ? cleanupMobs() : 0;
                    sender.sendMessage("§aLagBuster cleared " + removedDrops + " drops and " + removedMobs + " mobs.");
                }
                default -> {
                    sender.sendMessage("§cUsage: /lagclear [drops|mobs|all]");
                }
            }
            return true;
        }

        if ("tps".equalsIgnoreCase(command.getName())) {
            if (!sender.hasPermission("lagbuster.tps")) {
                sender.sendMessage("§cYou do not have permission to use this command.");
                return true;
            }

            double[] tps = getTps();
            if (tps[0] < 0) {
                sender.sendMessage("§cTPS info is not available on this server build.");
            } else {
                sender.sendMessage("§6[LagBuster] TPS: §a" + formatTps(tps[0]) + " §7(1m) §a" + formatTps(tps[1]) + " §7(5m) §a" + formatTps(tps[2]) + " §7(15m)");
            }
            return true;
        }

        return false;
    }

    private double[] getTps() {
        try {
            Object server = Bukkit.getServer();
            Method getTps = server.getClass().getMethod("getTPS");
            Object result = getTps.invoke(server);
            if (result instanceof double[] tps) {
                return tps;
            }
        } catch (Exception ignored) {
            // Not available on this server implementation
        }
        return new double[]{-1, -1, -1};
    }

    private static String formatTps(double value) {
        return String.format("%.2f", Math.min(20.0, value));
    }
}
