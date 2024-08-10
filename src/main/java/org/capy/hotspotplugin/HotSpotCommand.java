package org.capy.hotspotplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HotSpotCommand implements CommandExecutor {

    private static Location hotspotLocation = null;
    private static BukkitTask hotspotExpiryTask = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        FileConfiguration config = Utils.getConfig();
        String prefix = config.getString("prefix").replaceAll("&", "§");
        String color = config.getString("color").replaceAll("&", "§");
        String errorColor = config.getString("error-color").replaceAll("&", "§");

        String title = player.getName() + "'s Hotspot, /hotspot";
        BossBar bossBar = Bukkit.createBossBar(title, BarColor.PINK, BarStyle.SOLID);

        if (args.length == 0) {
            if (hotspotLocation == null) {
                player.sendMessage(prefix + errorColor + "There is no hotspot set.");
            } else {
                player.teleport(hotspotLocation);
                player.sendMessage(prefix + color + "Teleported to the hotspot.");
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("create")) {
            if (!player.hasPermission(config.getString("permissions.hotspot-create"))) {
                player.sendMessage(prefix + errorColor + "You do not have permission to create a hotspot.");
                return true;
            }

            if (hotspotLocation != null) {
                player.sendMessage(prefix + errorColor + "A hotspot is already set. Please wait for it to expire.");
                return true;
            }

            hotspotLocation = player.getLocation();
            for (Player p : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(player);

                if (p != player) {
                    p.sendMessage(prefix + color + "A hotspot has been created.");
                    p.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                }
            }
            player.sendMessage(prefix + color + "Hotspot created at your current location.");

            hotspotExpiryTask = new BukkitRunnable() {
                @Override
                public void run() {
                    hotspotLocation = null;
                    hotspotExpiryTask = null;

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(prefix + color + "Hotspot has Expired");
                        p.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        bossBar.removeAll();
                    }
                }
            }.runTaskLater(HotspotPlugin.getPlugin(), 30 * 20 * 60);

        } else {
            player.sendMessage(prefix + errorColor + "Invalid command usage.");
        }

        return true;
    }
}
