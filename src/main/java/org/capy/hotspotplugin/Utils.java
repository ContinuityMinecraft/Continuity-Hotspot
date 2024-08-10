package org.capy.hotspotplugin;

import org.bukkit.configuration.file.FileConfiguration;

public class Utils {
    public static FileConfiguration getConfig() {
        return HotspotPlugin.getPlugin().getConfig();
    }
}
