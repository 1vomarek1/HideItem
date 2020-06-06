package com.vomarek.hideitem.commands;

import com.vomarek.hideitem.data.PlayerState;
import com.vomarek.hideitem.HideItem;
import com.vomarek.hideitem.util.HidingItem;
import com.vomarek.hideitem.util.PlayerHiding;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;

public class Commands implements CommandExecutor {
    private HideItem plugin;
    private HashMap<String, Integer> cooldowns;

    public Commands (HideItem plugin) {
        this.plugin = plugin;
        cooldowns = new HashMap<>();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("HideItem")) {


            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reload")) return reloadConfig(sender);
                if (args[0].equalsIgnoreCase("info")) return info(sender);
                if (!plugin.getHideItemConfig().DISABLE_COMMANDS()) {
                    if (args[0].equalsIgnoreCase("hide")) return hide(sender);
                    if (args[0].equalsIgnoreCase("show")) return show(sender);
                    if (args[0].equalsIgnoreCase("toggle")) return toggle(sender);
                }
            }


        }


        return info(sender);
    }

    private boolean toggle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fOnly players can use this command!"));
            return true;
        }
        if (!sender.hasPermission("hideitem.toggle") && plugin.getHideItemConfig().REQUIRE_PERMISSION_FOR_COMMANDS()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().NO_PERMISSION_MESSAGE()));
            return true;
        }

        if (cooldowns.containsKey(sender.getName())) {

            if (((int) System.currentTimeMillis()/1000) - cooldowns.get(sender.getName()) < plugin.getHideItemConfig().COOLDOWN()) {

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().COOLDOWN_MESSAGE().replace("%cooldown%", String.valueOf(plugin.getHideItemConfig().COOLDOWN() - (((int) System.currentTimeMillis()/1000) - cooldowns.get(sender.getName()))))));
                return true;

            } else {
                cooldowns.remove(sender.getName());
            }

        }

        cooldowns.put(sender.getName(), (int)System.currentTimeMillis()/1000);

        final Player player = (Player) sender;

        final PlayerState playerState = plugin.getPlayerState();
        String state = playerState.getPlayerState(player);


        if (state == null) playerState.setPlayerState(player, plugin.getHideItemConfig().DEFAULT_SHOWN() ? "shown" : "hidden");
        if (state == null) state = plugin.getHideItemConfig().DEFAULT_SHOWN() ? "shown" : "hidden";


        if (state.equalsIgnoreCase("hidden")) {

            new PlayerHiding(plugin).show(player);

            playerState.setPlayerState(player, "shown");

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().SHOW_MESSAGE()));


            if (plugin.getHideItemConfig().DISABLE_ITEMS()) return true;
            new HidingItem(plugin).giveHideItem(player);

        } else if (state.equalsIgnoreCase("shown")){

            new PlayerHiding(plugin).hide(player);


            playerState.setPlayerState(player, "hidden");

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().HIDE_MESSAGE()));


            if (plugin.getHideItemConfig().DISABLE_ITEMS()) return true;

            new HidingItem(plugin).giveShowItem(player);

        }

        return true;
    }

    private boolean show(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fOnly players can use this command!"));
            return true;
        }
        if (!sender.hasPermission("hideitem.show") && plugin.getHideItemConfig().REQUIRE_PERMISSION_FOR_COMMANDS()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().NO_PERMISSION_MESSAGE()));
            return true;
        }

        if (cooldowns.containsKey(sender.getName())) {

            if (((int) System.currentTimeMillis()/1000) - cooldowns.get(sender.getName()) < plugin.getHideItemConfig().COOLDOWN()) {

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().COOLDOWN_MESSAGE().replace("%cooldown%", String.valueOf(plugin.getHideItemConfig().COOLDOWN() - (((int) System.currentTimeMillis()/1000) - cooldowns.get(sender.getName()))))));
                return true;

            } else {
                cooldowns.remove(sender.getName());
            }

        }

        cooldowns.put(sender.getName(), (int)System.currentTimeMillis()/1000);

        final Player player = (Player) sender;

        PlayerState playerState = plugin.getPlayerState();
        playerState.setPlayerState(player, "shown");

        new PlayerHiding(plugin).show(player);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().SHOW_MESSAGE()));

        if (plugin.getHideItemConfig().DISABLE_ITEMS()) return true;

        new HidingItem(plugin).giveHideItem(player);

        return true;
    }

    private boolean hide(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fOnly players can use this command!"));
            return true;
        }
        if (!sender.hasPermission("hideitem.hide") && plugin.getHideItemConfig().REQUIRE_PERMISSION_FOR_COMMANDS()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().NO_PERMISSION_MESSAGE()));
            return true;
        }

        if (cooldowns.containsKey(sender.getName())) {

            if (((int) System.currentTimeMillis()/1000) - cooldowns.get(sender.getName()) < plugin.getHideItemConfig().COOLDOWN()) {

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().COOLDOWN_MESSAGE().replace("%cooldown%", String.valueOf(plugin.getHideItemConfig().COOLDOWN() - (((int) System.currentTimeMillis()/1000) - cooldowns.get(sender.getName()))))));
                return true;

            } else {
                cooldowns.remove(sender.getName());
            }

        }

        cooldowns.put(sender.getName(), (int)System.currentTimeMillis()/1000);

        final Player player = (Player) sender;

        PlayerState playerState = plugin.getPlayerState();
        playerState.setPlayerState(player, "hidden");

        new PlayerHiding(plugin).hide(player);
        new HidingItem(plugin).giveShowItem(player);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().HIDE_MESSAGE()));

        return true;
    }

    private boolean info (CommandSender sender) {

        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem&7│ &fRunning &3HideItem&f v"+plugin.getDescription().getVersion()));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &fRunning &3HideItem&f v"+plugin.getDescription().getVersion()));
        }

        return true;
    }

    private boolean reloadConfig(CommandSender sender) {
        if (!sender.hasPermission("hideitem.reload")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getHideItemConfig().NO_PERMISSION_MESSAGE()));
            return true;
        }

        Date startTime = new Date();
        Date endTime = null;

        Runnable runnable = () -> {
            plugin.getHideItemConfig().reload();
            plugin.configReloaded();
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
            endTime = new Date();
        } catch (InterruptedException e) {
            e.printStackTrace();
            endTime = new Date();
        }

        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem&7│ &aSuccessfully reloaded config in %time% ms".replace("%time%", String.valueOf(endTime.getTime() - startTime.getTime()))));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &7| &aSuccessfully reloaded config in %time% ms".replace("%time%", String.valueOf(endTime.getTime() - startTime.getTime()))));
        }

        return true;
    }

}
