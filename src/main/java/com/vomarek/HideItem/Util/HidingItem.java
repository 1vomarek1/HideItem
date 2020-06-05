package com.vomarek.HideItem.Util;

import com.vomarek.HideItem.HideItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HidingItem {
    private HideItem plugin;

    public HidingItem(final HideItem plugin) {
        this.plugin = plugin;
    }

    public HidingItem giveHideItem(Player player) {
        for (int x = 0; x <= 8; x++) {
            ItemStack item = player.getInventory().getItem(x);

            if (item == null) continue;

            if (plugin.getHideItemConfig().isHideItem(item) || plugin.getHideItemConfig().isShowItem(item)) {

                final ItemStack hideItem = plugin.getHideItemConfig().HIDE_ITEM();

                player.getInventory().setItem(x, hideItem);
            }
        }
        return this;
    }

    public HidingItem giveShowItem(Player player) {
        for (int x = 0; x <= 8; x++) {
            ItemStack item = player.getInventory().getItem(x);

            if (item == null) continue;

            if (plugin.getHideItemConfig().isHideItem(item) || plugin.getHideItemConfig().isShowItem(item)) {

                final ItemStack hideItem = plugin.getHideItemConfig().SHOW_ITEM();

                player.getInventory().setItem(x, hideItem);
            }
        }
        return this;
    }


}
