package com.vomarek.hideitem;

import com.vomarek.hideitem.commands.Commands;
import com.vomarek.hideitem.commands.TabComplete;
import com.vomarek.hideitem.data.HideItemConfig;
import com.vomarek.hideitem.data.PlayerState;
import com.vomarek.hideitem.data.PlayersHidden;
import com.vomarek.hideitem.events.EventsClass;
import com.vomarek.hideitem.util.Cooldowns;
import com.vomarek.hideitem.util.HideItemStack;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class HideItem extends JavaPlugin {
    private static HideItem plugin;

    private boolean papi;

    private HideItemConfig config;
    private YamlConfiguration data;

    private PlayerState playerState;
    private PlayersHidden playersHidden;
    private Cooldowns cooldowns;
    private HideItemStack hideItemStack;

    @Override
    public void onEnable() {
        plugin = this;

        papi = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        if (papi) getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7|&b PlaceholderAPI&f found!"));
        else getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7|&b PlaceholderAPI&c not found&f! If you wish to use placeholders download it!"));

        Metrics metrics = new Metrics(this, 7853);

        if (metrics.isEnabled()) {
            playersHidden = new PlayersHidden(metrics);
        }

        config = new HideItemConfig(plugin);

        plugin.getServer().getPluginManager().registerEvents(new EventsClass(plugin), plugin);

        if (config.STORAGE_METHOD().equalsIgnoreCase("sqlite")) {
            final File file = new File(getDataFolder(), "data.db");

            if (!file.exists()) {
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (config.STORAGE_METHOD().equalsIgnoreCase("file")) {

            final File file = new File(getDataFolder(), "data.yml");

            try {
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                data = new YamlConfiguration();
                data.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

        } else data = null;

        playerState = new PlayerState(plugin);
        hideItemStack = new HideItemStack(plugin);

        cooldowns = new Cooldowns(plugin);

        getCommand("hideitem").setExecutor(new Commands(plugin));
        getCommand("hideitem").setTabCompleter(new TabComplete(plugin));

        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fHideItem has been enabled!"));
    }

    @Override
    public void onDisable() {
        if (getHideItemConfig().DATABASE() != null) getHideItemConfig().DATABASE().close();

        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fHideItem has been disabled!"));
    }

    /**
     * This method is used to get instance of HideItem plugin's main class.
     *
     * @return instance of HideItem's main class
     */
    public static HideItem getPlugin () {
        return plugin;
    }

    public boolean getPAPI() {
        return papi;
    }

    //region Configuration & data storage

    public HideItemConfig getHideItemConfig() {
        return config;
    }

    public YamlConfiguration getDataFile() {
        return data;
    }

    public void configReloaded() {
        playerState = new PlayerState(plugin);


        if (config.STORAGE_METHOD().equalsIgnoreCase("file")) {

            final File file = new File(getDataFolder(), "data.yml");

            try {
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                data = new YamlConfiguration();
                data.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

        } else data = null;
    }

    //endregion

    //region Plugins data

    public PlayerState getPlayerState() {
        return playerState;
    }

    public Cooldowns getCooldowns() {
        return cooldowns;
    }

    public PlayersHidden getPlayersHidden() {
        return playersHidden;
    }

    public HideItemStack getHideItemStack() {
        return hideItemStack;
    }

    //endregion

}
