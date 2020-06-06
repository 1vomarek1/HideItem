package com.vomarek.hideitem.util;

import com.vomarek.hideitem.HideItem;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class PlayerHiding {
    private HideItem plugin;

    public PlayerHiding(final HideItem plugin) {
        this.plugin = plugin;
    }

    public void hideSinglePlayer(final Player player, final Player target) {
        player.hidePlayer(plugin, target);
    }

    public void showSinglePlayer(final Player player, final Player target) {
        if (isVanished(target) && !player.hasPermission("hideitem.seevanished")) return;
        player.hidePlayer(plugin, target);
    }

    public void hide(final Player player) {
        for (final Player p : plugin.getServer().getOnlinePlayers()) {
            player.hidePlayer(plugin, p);
        }
    }

    public void show(final Player player) {
        for (final Player p : plugin.getServer().getOnlinePlayers()) {
            if (isVanished(p) && !player.hasPermission("hideitem.seevanished")) continue;
            player.showPlayer(plugin, p);
        }
    }

    private boolean isVanished(final Player player) {
        for (final MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
