package com.vomarek.hideitem.events;

import com.vomarek.hideitem.data.PlayerState;
import com.vomarek.hideitem.HideItem;
import com.vomarek.hideitem.util.HidingItem;
import com.vomarek.hideitem.util.PlayerHiding;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class EventsClass implements Listener {
    private final HideItem plugin;

    public EventsClass(final HideItem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(final PlayerInteractEvent event) {

        final Player player = event.getPlayer();

        try {

            if (Integer.parseInt(plugin.getServer().getVersion().split("\\.")[1]) > 8) {
                if (!event.getHand().equals(EquipmentSlot.HAND)) return;
            }

        } catch (final NumberFormatException ignored) {}


        if (event.getItem() == null) return;

        ItemStack i = event.getItem();

        if (!plugin.getHideItemConfig().isHideItem(i) && !plugin.getHideItemConfig().isShowItem(i)) return;

        event.setCancelled(true);

        if (!event.getAction().equals(Action.LEFT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (!player.hasPermission("hideitem.toggle") && plugin.getHideItemConfig().REQUIRE_PERMISSION_FOR_ITEMS()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().NO_PERMISSION_MESSAGE()));
            return;
        }

        if (plugin.getCooldowns().isOnCooldown(player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().COOLDOWN_MESSAGE().replace("%cooldown%", String.valueOf(plugin.getCooldowns().getCooldown(player.getUniqueId().toString())))));
            return;
        }

        plugin.getCooldowns().setCooldown(player.getUniqueId().toString());


        final PlayerState playerState = plugin.getPlayerState();
        String state = playerState.getPlayerState(player);


        if (state == null) playerState.setPlayerState(player, plugin.getHideItemConfig().DEFAULT_SHOWN() ? "shown" : "hidden");
        if (state == null) state = plugin.getHideItemConfig().DEFAULT_SHOWN() ? "shown" : "hidden";


        if (state.equalsIgnoreCase("hidden")) {

            new PlayerHiding(plugin).show(player);


            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().SHOW_MESSAGE()));


            if (!plugin.getHideItemConfig().DISABLE_ITEMS()) new HidingItem(plugin).giveHideItem(player);

            new BukkitRunnable(){

                @Override
                public void run() {
                    playerState.setPlayerState(player, "shown");
                }
            }.runTaskAsynchronously(plugin);

        } else if (state.equalsIgnoreCase("shown")){

            new PlayerHiding(plugin).hide(player);



            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().HIDE_MESSAGE()));


            if (!plugin.getHideItemConfig().DISABLE_ITEMS()) new HidingItem(plugin).giveShowItem(player);

            new BukkitRunnable(){

                @Override
                public void run() {
                    playerState.setPlayerState(player, "hidden");
                }
            }.runTaskAsynchronously(plugin);

        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInteractEntity(final PlayerInteractEntityEvent event){
        ItemStack i;
        try {

            if (Integer.parseInt(plugin.getServer().getVersion().split("\\.")[1]) > 8) {
                i = event.getPlayer().getInventory().getItemInMainHand();
            } else {
                i = event.getPlayer().getInventory().getItemInHand();
            }

        } catch (NumberFormatException ignored) {
            i = event.getPlayer().getInventory().getItemInHand();
        }

        if (plugin.getHideItemConfig().isShowItem(i) || plugin.getHideItemConfig().isHideItem(i)) event.setCancelled(true);
    }


    @EventHandler
    public void PlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        final String state = plugin.getPlayerState().getPlayerState(player);

        boolean hasHiddenPlayers;

        if (Arrays.asList("hidden", "shown").contains(state)) hasHiddenPlayers = state.equalsIgnoreCase("hidden"); else hasHiddenPlayers = !plugin.getHideItemConfig().DEFAULT_SHOWN();

        final ItemStack hideItem = hasHiddenPlayers ? plugin.getHideItemConfig().SHOW_ITEM() : plugin.getHideItemConfig().HIDE_ITEM();

        // Hide Players
        if (hasHiddenPlayers) {
            new PlayerHiding(plugin).hide(player);
        }

        if (plugin.getHideItemConfig().DISABLE_ITEMS()) return;

        // Give hide / show item to correct slot
        if (plugin.getHideItemConfig().FIRST_FREE_SLOT()) {
            for (ItemStack i : player.getInventory().getContents()) {
                if (i == null) continue;
                if (plugin.getHideItemConfig().isHideItem(i) || plugin.getHideItemConfig().isShowItem(i)) return;
            }

            player.getInventory().addItem(hideItem);

        } else {

            player.getInventory().setItem(plugin.getHideItemConfig().ITEM_SLOT() - 1, hideItem);

        }
    }

    @EventHandler
    public void hidePlayer(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        for (final Player p : plugin.getServer().getOnlinePlayers()) {
            final String state = plugin.getPlayerState().getPlayerState(p);

            if (state == null) continue;

            if (state.equalsIgnoreCase("hidden")) {
                new PlayerHiding(plugin).hideSinglePlayer(p, player);
            } else if (state.equalsIgnoreCase("shown")) {
                new PlayerHiding(plugin).showSinglePlayer(p, player);
            }
            
        }

    }


    @EventHandler (ignoreCancelled = true)
    public void onDrop(final PlayerDropItemEvent event) {
        if (plugin.getHideItemConfig().DISABLE_ITEMS()) return;
        if (!plugin.getHideItemConfig().FIXED_ITEM()) return;

        if (event.getItemDrop() == null) return;

        if (event.getItemDrop().getItemStack() == null) return;

        final ItemStack i = event.getItemDrop().getItemStack();

        event.setCancelled(plugin.getHideItemConfig().isHideItem(i) || plugin.getHideItemConfig().isShowItem(i));

    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        if (plugin.getHideItemConfig().DISABLE_ITEMS()) return;

        if (event.getDrops() == null) return;

        for (final ItemStack i : event.getEntity().getInventory().getContents()) {

            if (i == null) continue;

            if (!i.hasItemMeta()) continue;

            if (!plugin.getHideItemConfig().isHideItem(i) && !plugin.getHideItemConfig().isShowItem(i)) continue;

            event.getDrops().remove(i);
        }
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent event) {
        if (plugin.getHideItemConfig().DISABLE_ITEMS()) return;

        final Player player = event.getPlayer();

        final String state = plugin.getPlayerState().getPlayerState(player);

        boolean hasHiddenPlayers;

        if (Arrays.asList("hidden", "shown").contains(state)) hasHiddenPlayers = state.equalsIgnoreCase("hidden"); else hasHiddenPlayers = !plugin.getHideItemConfig().DEFAULT_SHOWN();


        final ItemStack hideItem = hasHiddenPlayers ? plugin.getHideItemConfig().SHOW_ITEM() : plugin.getHideItemConfig().HIDE_ITEM();

        if (plugin.getHideItemConfig().FIRST_FREE_SLOT()) {
            player.getInventory().addItem(hideItem);
        } else {
            player.getInventory().setItem(plugin.getHideItemConfig().ITEM_SLOT() - 1, hideItem);
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void InventoryClick(final InventoryClickEvent event) {
        if (plugin.getHideItemConfig().DISABLE_ITEMS()) return;
        if (!plugin.getHideItemConfig().FIXED_ITEM()) return;

        if (event.getCurrentItem() != null) {

            if (plugin.getHideItemConfig().isShowItem(event.getCurrentItem()) || plugin.getHideItemConfig().isHideItem(event.getCurrentItem())) {
                event.setCancelled(true);
            }
        }

        if (event.getCursor() != null) {
            if (plugin.getHideItemConfig().isShowItem(event.getCursor()) || plugin.getHideItemConfig().isHideItem(event.getCursor())) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        if (plugin.getHideItemConfig().DISABLE_COMMANDS()) return;
        if (!plugin.getHideItemConfig().USE_ALIASES()) return;

        if (event.getMessage().equalsIgnoreCase(plugin.getHideItemConfig().HIDE_ALIAS())) event.setMessage("/hideitem hide");
        if (event.getMessage().equalsIgnoreCase(plugin.getHideItemConfig().SHOW_ALIAS())) event.setMessage("/hideitem show");
        if (event.getMessage().equalsIgnoreCase(plugin.getHideItemConfig().TOGGLE_ALIAS())) event.setMessage("/hideitem toggle");
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onTab(final TabCompleteEvent event) {
        final String buffer = event.getBuffer().toLowerCase();

        final List<String> completions = event.getCompletions();

        if (plugin.getHideItemConfig().TOGGLE_ALIAS().startsWith(buffer)) completions.add(plugin.getHideItemConfig().TOGGLE_ALIAS());
        if (plugin.getHideItemConfig().HIDE_ALIAS().startsWith(buffer)) completions.add(plugin.getHideItemConfig().HIDE_ALIAS());
        if (plugin.getHideItemConfig().SHOW_ALIAS().startsWith(buffer)) completions.add(plugin.getHideItemConfig().SHOW_ALIAS());

        event.setCompletions(completions);

    }

}
