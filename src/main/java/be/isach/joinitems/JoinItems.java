package be.isach.joinitems;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Sacha on 19/10/15.
 */
public class JoinItems extends JavaPlugin {

    PluginDescriptionFile pluginDescriptionFile;
    public static JoinItems instance;
    private static FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        pluginDescriptionFile = getDescription();
        instance = this;
        config = getConfig();

        JoinItem.canBeDropped = getConfig().getBoolean("Items-Settings.Allow-Drop");
        JoinItem.canBeMoved = getConfig().getBoolean("Items-Settings.Allow-Move");
        JoinItem.canBePlaced = getConfig().getBoolean("Items-Settings.Allow-Place");
        JoinItem.isGivenOnRespawn = getConfig().getBoolean("Items-Settings.Give-On-Respawn");
        JoinItem.isDroppedOnDeath = getConfig().getBoolean("Items-Settings.Drop-On-Death");

        registerListener(new PlayerListener());
        Async.run(() -> {
            ItemsManager.loadJoinItems();
            Bukkit.getOnlinePlayers().forEach(player -> ItemsManager.giveJoinItems(player));
        });

        log(getPluginName() + " v" + getVersion() + " by " + getAuthor() + " loaded and enabled.");

    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public static String getString(String path, boolean formatColor) {
        String notFormatted = config().getString(path);
        if (formatColor)
            notFormatted = ChatColor.translateAlternateColorCodes('&', notFormatted);
        return notFormatted;
    }

    private String getVersion() {
        return pluginDescriptionFile.getVersion();
    }

    private String getPluginName() {
        return pluginDescriptionFile.getName();
    }

    private String getAuthor() {
        return pluginDescriptionFile.getAuthors().get(0);
    }

    public static void log(Object object) {
        Bukkit.getServer().getLogger().info(object.toString());
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> ItemsManager.removeJoinItems(player));
        JoinItem.joinItems.clear();
    }

    public static FileConfiguration config() {
        return config;
    }
}
