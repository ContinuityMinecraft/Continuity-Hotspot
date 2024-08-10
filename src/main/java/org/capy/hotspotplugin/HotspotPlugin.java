package org.capy.hotspotplugin;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public final class HotspotPlugin extends JavaPlugin {
    Logger logger = getLogger();
    private static HotspotPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        this.getCommand("hotspot").setExecutor(new HotSpotCommand());
        Bukkit.getServer().getPluginManager().addPermission(new Permission(Utils.getConfig().getString("permissions.hotspot-create")));
        Bukkit.getServer().getPluginManager().addPermission(new Permission(Utils.getConfig().getString("permissions.hotspot-tp")));
        logger.info("Loaded succesfully");
    }

    public static HotspotPlugin getPlugin() {
        return instance;
    }
}
