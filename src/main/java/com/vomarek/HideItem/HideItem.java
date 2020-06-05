package com.vomarek.HideItem;

import com.vomarek.HideItem.Commands.Commands;
import com.vomarek.HideItem.Data.HideItemConfig;
import com.vomarek.HideItem.Data.PlayerState;
import com.vomarek.HideItem.Events.EventsClass;
import com.vomarek.HideItem.Util.HidingItem;
import com.vomarek.HideItem.Util.PlayerHiding;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class HideItem extends JavaPlugin {
    private static HideItem plugin;
    private HideItemConfig config;
    private YamlConfiguration data;
    private PlayerState playerState;

    @Override
    public void onEnable() {
        plugin = this;


        config = new HideItemConfig(plugin);

        plugin.getServer().getPluginManager().registerEvents(new EventsClass(plugin), plugin);

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

        getCommand("hideitem").setExecutor(new Commands(plugin));

        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fHideItem has been enabled!"));
    }

    @Override
    public void onDisable() {
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fHideItem has been disabled!"));
    }



    public static void setHiddenState(Player player, Boolean hidden) {
        if (hidden) {
            new PlayerHiding(plugin).hide(player);
            new HidingItem(plugin).giveHideItem(player);
            plugin.getPlayerState().setPlayerState(player, "hidden");
        } else {
            new PlayerHiding(plugin).show(player);
            new HidingItem(plugin).giveShowItem(player);
            plugin.getPlayerState().setPlayerState(player, "shown");
        }
    }

    public static void hideFor(Player player) {
        new PlayerHiding(plugin).hide(player);
        new HidingItem(plugin).giveHideItem(player);
        plugin.getPlayerState().setPlayerState(player, "hidden");
    }

    public static void showFor(Player player) {
        new PlayerHiding(plugin).show(player);
        new HidingItem(plugin).giveShowItem(player);
        plugin.getPlayerState().setPlayerState(player, "shown");
    }


    public HideItemConfig getHideItemConfig() {
        return config;
    }

    public YamlConfiguration getDataFile() {
        return data;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void configReloaded() {
        playerState = new PlayerState(plugin);
    }
}
