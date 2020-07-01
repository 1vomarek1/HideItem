package com.vomarek.hideitem.util;

import com.vomarek.hideitem.HideItem;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Cooldowns {
    private HideItem plugin;
    private HashMap<String, Integer> cooldowns;

    public Cooldowns(final HideItem plugin) {
        this.plugin = plugin;
        cooldowns = new HashMap<>();
    }

    public void setCooldown(@NotNull final String uuid) {
        cooldowns.put(uuid, (int)System.currentTimeMillis()/1000);
    }

    public Boolean isOnCooldown (@NotNull final String uuid) {
        if (!cooldowns.containsKey(uuid)) return false;

        if (cooldowns.containsKey(uuid)) {

            if (((int) System.currentTimeMillis() / 1000) - cooldowns.get(uuid) < plugin.getHideItemConfig().COOLDOWN()) {

                return true;

            }
        }

        return false;
    }

    public void unsetCooldown(@NotNull final String uuid) {
        cooldowns.remove(uuid);
    }

    public Integer getCooldown (@NotNull final String uuid) {
        if (!cooldowns.containsKey(uuid)) return null;
        return plugin.getHideItemConfig().COOLDOWN() - (((int) System.currentTimeMillis() / 1000) - cooldowns.get(uuid));
    }
}
