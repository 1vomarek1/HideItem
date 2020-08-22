package com.vomarek.hideitem.util;

import com.vomarek.hideitem.HideItem;
import com.vomarek.spigotutils.nbt.NBTTags;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HidingItem {
    private HideItem plugin;

    public HidingItem(final HideItem plugin) {
        this.plugin = plugin;
    }

    public HidingItem giveHideItem(Player player) {
        if (plugin.getHideItemConfig().DISABLE_ITEMS()) return this;

        for (int x = 0; x <= 8; x++) {
            ItemStack item = player.getInventory().getItem(x);

            if (item == null) continue;

            if (NBTTags.getBoolean(item, "HIDE_ITEM") || NBTTags.getBoolean(item, "SHOW_ITEM")) {

                final ItemStack hideItem = plugin.getHideItemConfig().HIDE_ITEM();

                player.getInventory().setItem(x, hideItem);
            }
        }
        return this;
    }

    public HidingItem giveShowItem(Player player) {
        if (plugin.getHideItemConfig().DISABLE_ITEMS()) return this;

        for (int x = 0; x <= 8; x++) {
            ItemStack item = player.getInventory().getItem(x);

            if (item == null) continue;

            if (NBTTags.getBoolean(item, "HIDE_ITEM") || NBTTags.getBoolean(item, "SHOW_ITEM")) {

                final ItemStack hideItem = plugin.getHideItemConfig().SHOW_ITEM();

                player.getInventory().setItem(x, hideItem);
            }
        }
        return this;
    }


}
