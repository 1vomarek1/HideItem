package com.vomarek.hideitem;

import com.vomarek.hideitem.util.HidingItem;
import com.vomarek.hideitem.util.PlayerHiding;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HideItemAPI {
    private final static HideItem plugin = HideItem.getPlugin();

    /**
     * Using this method you can set visibility of other players for player.<br>
     * Player will see vanished players if has hideitem.seevanished permission.<br>
     * No message is sent to player!
     *
     * @param player who to set visibility of others to
     * @param hidden should player have hidden players?
     */

    public static void setHiddenState(final Player player, final Boolean hidden) {
        if (hidden) {
            new PlayerHiding(plugin).hide(player);
            new HidingItem(plugin).giveHideItem(player);
            new BukkitRunnable() {

                @Override
                public void run () {
                    plugin.getPlayerState().setPlayerState(player, "hidden");
                }
            }.runTaskAsynchronously(plugin);
        } else {
            new PlayerHiding(plugin).show(player);
            new HidingItem(plugin).giveShowItem(player);
            new BukkitRunnable() {

                @Override
                public void run () {
                    plugin.getPlayerState().setPlayerState(player, "shown");
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    /**
     * Using this method you can hide players for specific player.<br>
     * No message is sent to player!
     *
     * @param player Player who you want to hide others to
     */
    public static void hideFor(final Player player) {
        new PlayerHiding(plugin).hide(player);
        new HidingItem(plugin).giveHideItem(player);
        new BukkitRunnable() {

            @Override
            public void run () {
                plugin.getPlayerState().setPlayerState(player, "hidden");
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Using this method you can show players for specific player.<br>
     * Player will see vanished players if has hideitem.seevanished permission.<br>
     * No message is sent to player!
     *
     * @param player Player who you want to show others to
     */
    public static void showFor(final Player player) {
        new PlayerHiding(plugin).show(player);
        new HidingItem(plugin).giveShowItem(player);
        new BukkitRunnable() {

            @Override
            public void run () {
                plugin.getPlayerState().setPlayerState(player, "shown");
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * With this method you can remove all hide/show items from players inventory
     *
     * @param player Player who you want to remove hide/show items from
     */
    public static void removeItems(final Player player) {
        final ItemStack hideitem = plugin.getHideItemConfig().HIDE_ITEM();
        final ItemStack showitem = plugin.getHideItemConfig().SHOW_ITEM();

        while (true) if (player.getInventory().removeItem(hideitem).size() != 0) break;
        while (true) if (player.getInventory().removeItem(showitem).size() != 0) break;
    }

}
