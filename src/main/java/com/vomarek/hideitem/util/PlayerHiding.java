package com.vomarek.hideitem.util;

import com.vomarek.hideitem.HideItem;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.Arrays;

public class PlayerHiding {
    private HideItem plugin;
    private Boolean usePlugin;

    public PlayerHiding(final HideItem plugin) {
        this.plugin = plugin;

        final String[] arr = plugin.getServer().getBukkitVersion().split("\\.");

        if (arr.length >= 2) {

            try {
                Integer i = Integer.parseInt(arr[1]);

                if (i > 11) usePlugin = true;
                else usePlugin = false;
            } catch (NumberFormatException ignored) {
                usePlugin = false;
            }
        } else {
            usePlugin = false;
        }
    }

    @SuppressWarnings("deprecation")
    public void hideSinglePlayer(final Player player, final Player target) {
        if (usePlugin) {
            player.hidePlayer(plugin, target);
        } else {
            player.hidePlayer(target);
        }
    }

    @SuppressWarnings("deprecation")
    public void showSinglePlayer(final Player player, final Player target) {
        if (isVanished(target) && !player.hasPermission("hideitem.seevanished")) return;
        if (usePlugin) {
            player.showPlayer(plugin, target);
        } else {
            player.showPlayer(target);
        }
    }

    @SuppressWarnings("deprecation")
    public void hide(final Player player) {
        for (final Player p : plugin.getServer().getOnlinePlayers()) {
            if (usePlugin) {
                player.hidePlayer(plugin, p);
            } else {
                player.hidePlayer(p);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void show(final Player player) {
        for (final Player p : plugin.getServer().getOnlinePlayers()) {
            if (isVanished(p) && !player.hasPermission("hideitem.seevanished")) continue;
            if (usePlugin) {
                player.showPlayer(plugin, p);
            } else {
                player.showPlayer(p);
            }
        }
    }

    private boolean isVanished(final Player player) {
        for (final MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
