package com.vomarek.HideItem.Data;

import com.vomarek.HideItem.HideItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HideItemConfig {
    private HideItem plugin;
    private YamlConfiguration config;

    private String STORAGE_METHOD;
    private MySQL MYSQL;

    private Boolean LOBBY_SERVER;
    private Boolean DISABLE_ITEMS;
    private Boolean DISABLE_COMMANDS;
    private Boolean DEFAULT_SHOWN;

    private ItemStack HIDE_ITEM;
    private ItemStack SHOW_ITEM;
    private Boolean FIRST_FREE_SLOT;
    private Integer ITEM_SLOT;
    private Boolean FIXED_ITEM;

    private String SHOW_MESSAGE;
    private String HIDE_MESSAGE;
    private String COOLDOWN_MESSAGE;
    private String NO_PERMISSION_MESSAGE;

    private Integer COOLDOWN;

    private Boolean USE_ALIASES;
    private String HIDE_ALIAS;
    private String SHOW_ALIAS;
    private String TOGGLE_ALIAS;

    public HideItemConfig(HideItem plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {

        try {

            final File file = new File(plugin.getDataFolder(), "config.yml");

            // If config doesn't exist create new one
            if (!file.exists()) {

                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            config = new YamlConfiguration();
            config.load(file);


            // If config is empty copy default one
            if (config.getConfigurationSection("").getKeys(true).isEmpty()) {
                Reader defConfigStream = new InputStreamReader(this.plugin.getResource("config.yml"), StandardCharsets.UTF_8);
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                    config.setDefaults(defConfig);

                    plugin.saveResource("config.yml", true);

                    config.load(file);
                }

            } else {

                if (config.getString("version") != plugin.getDescription().getVersion()) {
                    if (!config.getBoolean("rename-old-config", true)) {

                        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &f| &cYou have outdated config in '/plugins/HideItem/' please update it!"));

                    } else {
                        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &f| &cYou have outdated config in '/plugins/HideItem/' New one will be created! (Your current one will be saved as config.old.yml)"));

                        File oldFile = new File(plugin.getDataFolder(), "config.old.yml");

                        if (oldFile.exists()) {
                            plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lHideItem &f| &cconfig.old.yml already exists in '/plugins/HideItem/'! Please delete it so new config can be generated!"));

                        } else {
                            file.renameTo(oldFile);

                            Reader defConfigStream = new InputStreamReader(this.plugin.getResource("config.yml"), StandardCharsets.UTF_8);
                            if (defConfigStream != null) {
                                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                                config.setDefaults(defConfig);

                                plugin.saveResource("config.yml", true);

                                config.load(file);
                            }
                        }
                    }
                }
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Storage method
        STORAGE_METHOD = config.getString("storage-method", "none").toLowerCase();

        // Setup data File if not exists
        if (STORAGE_METHOD.equals("file")) {

            final File file = new File(plugin.getDataFolder(),"data.yml");

            try {

                // Create new if doesn't exist
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // Setup MySQL if storage method is mysql
        if (STORAGE_METHOD.equals("mysql")) {

            String MYSQL_HOST = config.getString("mysql.host", "localhost");
            Integer MYSQL_PORT = config.getInt("mysql.port", 3306);
            String MYSQL_DATABASE = config.getString("mysql.database", "HideItem");
            String MYSQL_TABLE = config.getString("mysql.table", "HideItem");
            String MYSQL_USER = config.getString("mysql.user", "root");
            String MYSQL_PASSWORD = config.getString("mysql.password", "");

            MYSQL = new MySQL(MYSQL_HOST, MYSQL_PORT, MYSQL_DATABASE, MYSQL_TABLE, MYSQL_USER, MYSQL_PASSWORD);

        }


        // Lobby server
        LOBBY_SERVER = config.getBoolean("lobby-server", false);

        // Disabled feartures
        DISABLE_ITEMS = config.getBoolean("disable-items", false);
        DISABLE_COMMANDS = config.getBoolean("disable-commands", false);

        // Items

        // Hide item
        Material hideMaterial = Material.getMaterial(config.getString("hide-item.material", "INK_SACK:0").split(":")[1]);
        if (hideMaterial == null) hideMaterial = Material.INK_SACK;

        HIDE_ITEM = new ItemStack(hideMaterial, 1, (byte) Integer.parseInt(config.getString("hide-item.material", "INK_SACK:0").split(":")[1]));
        ItemMeta hideItemMeta = HIDE_ITEM.getItemMeta();

        hideItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("hide-item.name", "&eHide Players &7(Shown)")));

        ArrayList<String> hideItemLore = (ArrayList<String>) config.getStringList("hide-item.lore");

        for (int i = 0; i < hideItemLore.size(); i++) {
            hideItemLore.set(i, ChatColor.translateAlternateColorCodes('&', hideItemLore.get(i)));
        }

        hideItemMeta.setLore(hideItemLore);

        HIDE_ITEM.setItemMeta(hideItemMeta);

        // Show Item
        Material showMaterial = Material.getMaterial(config.getString("show-item.material", "INK_SACK:0").split(":")[1]);
        if (showMaterial == null) showMaterial = Material.INK_SACK;

        SHOW_ITEM = new ItemStack(showMaterial, 1, (byte) Integer.parseInt(config.getString("show-item.material", "INK_SACK:0").split(":")[1]));
        ItemMeta showItemMeta = SHOW_ITEM.getItemMeta();

        showItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("show-item.name", "&eHide Players &7(Hidden)")));

        ArrayList<String> showItemLore = (ArrayList<String>) config.getStringList("show-item.lore");

        for (int i = 0; i < showItemLore.size(); i++) {
            showItemLore.set(i, ChatColor.translateAlternateColorCodes('&', showItemLore.get(i)));
        }

        showItemMeta.setLore(showItemLore);

        SHOW_ITEM.setItemMeta(showItemMeta);

        FIRST_FREE_SLOT = config.getBoolean("first-free-slot", false);
        ITEM_SLOT = config.getInt("item-slot", 8);
        FIXED_ITEM = config.getBoolean("fixed-item", true);

        SHOW_MESSAGE = config.getString("show-message", "&aAll players are now visible!");
        HIDE_MESSAGE = config.getString("hide-message", "&aAll players are now hidden!");
        COOLDOWN_MESSAGE = config.getString("cooldown-message", "&eYou are in cooldown for %cooldown% more seconds.");
        NO_PERMISSION_MESSAGE = config.getString("no-permission-message", "&eYou don't have permission to do that!");


        // Other settings
        COOLDOWN = config.getInt("cooldown", 5);
        DEFAULT_SHOWN = config.getBoolean("default-state-shown", true);

        // Commands & aliases
        USE_ALIASES = config.getBoolean("use-aliases", true);
        SHOW_ALIAS = config.getString("show-command-alias", "/show");
        HIDE_ALIAS = config.getString("hide-command-alias", "/hide");
        TOGGLE_ALIAS = config.getString("toggle-command-alias", "/toggleplayers");
    }

    public void reload() {
        loadConfig();
    }

    public Boolean LOBBY_SERVER() {
        return LOBBY_SERVER;
    }

    public Boolean DISABLE_ITEMS() {
        return DISABLE_ITEMS;
    }

    public Boolean DISABLE_COMMANDS() {
        return DISABLE_COMMANDS;
    }

    public String STORAGE_METHOD() {
        return STORAGE_METHOD;
    }

    public MySQL MYSQL() {
        return MYSQL;
    }

    public Boolean DEFAULT_SHOWN() {
        return DEFAULT_SHOWN;
    }

    public ItemStack HIDE_ITEM() {
        return HIDE_ITEM;
    }


    public boolean isHideItem(ItemStack i) {
        if (!i.hasItemMeta()) return false;

        return i.equals(HIDE_ITEM);
    }

    public ItemStack SHOW_ITEM() {
        return SHOW_ITEM;
    }

    public boolean isShowItem(ItemStack i) {
        if (!i.hasItemMeta()) return false;

        return i.equals(SHOW_ITEM);
    }

    public String SHOW_MESSAGE() {
        return SHOW_MESSAGE;
    }

    public String HIDE_MESSAGE() {
        return HIDE_MESSAGE;
    }

    public String COOLDOWN_MESSAGE() {
        return COOLDOWN_MESSAGE;
    }

    public String NO_PERMISSION_MESSAGE() {
        return NO_PERMISSION_MESSAGE;
    }

    public Boolean FIRST_FREE_SLOT() {
        return FIRST_FREE_SLOT;
    }

    public Integer ITEM_SLOT() {
        return ITEM_SLOT;
    }

    public Boolean FIXED_ITEM() {
        return FIXED_ITEM;
    }

    public Integer COOLDOWN() {
        return COOLDOWN;
    }


    public Boolean USE_ALIASES() {
        return USE_ALIASES;
    }

    public String SHOW_ALIAS() {
        return SHOW_ALIAS;
    }

    public String HIDE_ALIAS() {
        return HIDE_ALIAS;
    }

    public String TOGGLE_ALIAS() {
        return TOGGLE_ALIAS;
    }
}
