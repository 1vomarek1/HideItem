package com.vomarek.hideitem.commands;

import com.vomarek.hideitem.HideItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TabComplete implements TabCompleter {
    private HideItem plugin;

    public TabComplete(@NotNull final HideItem plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (command.getName().equalsIgnoreCase("hideitem")) {

            System.out.println(command.getName());

            if (sender.hasPermission("hideitem.reload")) {
                if (!plugin.getHideItemConfig().DISABLE_COMMANDS())
                    return Arrays.asList("info", "reload", "toggle", "show", "hide");

                return Arrays.asList("info", "reload");
            } else {
                if (!plugin.getHideItemConfig().DISABLE_COMMANDS())
                    return Arrays.asList("info", "toggle", "show", "hide");

                return Arrays.asList("info");
            }
        }


        return null;
    }
}
