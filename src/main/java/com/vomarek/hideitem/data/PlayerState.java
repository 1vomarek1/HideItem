package com.vomarek.hideitem.data;

import com.vomarek.hideitem.HideItem;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PlayerState {
    private HideItem plugin;
    private String STORAGE_TYPE;

    private HashMap<String, String> playerStates;

    public PlayerState(HideItem plugin) {
        this.plugin = plugin;
        this.STORAGE_TYPE = plugin.getHideItemConfig().STORAGE_METHOD();

        playerStates = new HashMap<>();
    }

    public PlayerState setPlayerState(Player player, String state) {
        playerStates.put(player.getName(), state);

        if (STORAGE_TYPE.equalsIgnoreCase("none")) return this;

        if (STORAGE_TYPE.equalsIgnoreCase("file")) {
            if (plugin.getDataFile() == null) return this;

            try {
                plugin.getDataFile().set(player.getName(), state);
                plugin.getDataFile().save(new File(plugin.getDataFolder(),"data.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (STORAGE_TYPE.equalsIgnoreCase("MySQL")) {
            if (plugin.getHideItemConfig().MYSQL() == null) return this;
            plugin.getHideItemConfig().MYSQL().setState(player, state);
        }

        return this;
    }

    public String getPlayerState(Player player) {
        if (playerStates.containsKey(player.getName())) return playerStates.get(player.getName());

        if (STORAGE_TYPE.equalsIgnoreCase("none")) return null;

        if (STORAGE_TYPE.equalsIgnoreCase("file")) {
            if (plugin.getDataFile() == null) return null;

            final String state = plugin.getDataFile().getString(player.getName(), "");

            if (state.equals("")) return null;

            return state;

        }

        if (STORAGE_TYPE.equalsIgnoreCase("MySQL")) {
            if (plugin.getHideItemConfig().MYSQL() == null) return null;

            final String state = plugin.getHideItemConfig().MYSQL().getState(player);

            if (state == null) return null;

            return state;
        }

        return null;
    }
}
