package com.vomarek.hideitem;

import com.vomarek.hideitem.commands.Commands;
import com.vomarek.hideitem.commands.TabComplete;
import com.vomarek.hideitem.data.HideItemConfig;
import com.vomarek.hideitem.data.PlayerState;
import com.vomarek.hideitem.data.PlayersHidden;
import com.vomarek.hideitem.events.EventsClass;
import com.vomarek.hideitem.util.HidingItem;
import com.vomarek.hideitem.util.PlayerHiding;
import org.bstats.bukkit.Metrics;
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

    private PlayersHidden playersHidden;

    @Override
    public void onEnable() {
        plugin = this;

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

        getCommand("hideitem").setExecutor(new Commands(plugin));
        getCommand("hideitem").setTabCompleter(new TabComplete(plugin));

        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fHideItem has been enabled!"));
    }

    @Override
    public void onDisable() {
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fHideItem has been disabled!"));
    }

    /**
     * Using this method you can set visibility of other players for player.<br>
     * Player will see vanished players if has hideitem.seevanished permission.<br>
     * No message is sent to player!
     *
     * @param player who to set visibility of others to
     * @param hidden should player have hidden players?
     */

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

    /**
     * Using this method you can hide players for specific player.<br>
     * No message is sent to player!
     *
     * @param player Player who you want to hide others to
     */
    public static void hideFor(Player player) {
        new PlayerHiding(plugin).hide(player);
        new HidingItem(plugin).giveHideItem(player);
        plugin.getPlayerState().setPlayerState(player, "hidden");
    }

    /**
     * Using this method you can show players for specific player.<br>
     * Player will see vanished players if has hideitem.seevanished permission.<br>
     * No message is sent to player!
     *
     * @param player Player who you want to show others to
     */
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

    public PlayersHidden getPlayersHidden() {
        return playersHidden;
    }
}
