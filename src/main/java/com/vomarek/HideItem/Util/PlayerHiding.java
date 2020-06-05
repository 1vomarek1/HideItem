package com.vomarek.HideItem.Util;

import com.vomarek.HideItem.HideItem;
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
        if (isVanished(target)) return;
        player.hidePlayer(plugin, target);
    }

    public void hide(final Player player) {
        for (final Player p : plugin.getServer().getOnlinePlayers()) {
            player.hidePlayer(plugin, p);
        }
    }

    public void show(final Player player) {
        for (final Player p : plugin.getServer().getOnlinePlayers()) {
            if (isVanished(p)) continue;
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
