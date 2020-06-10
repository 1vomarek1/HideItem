# HideItem
**by 1vomarek1**    
HideItem is spigot plugin used to give players option to toggle visibility of other players.  
Documentation [link](https://docs.vomarek.com/hideitem/hideitem)  
This plugin was originally created by **qKing12** [Link](https://www.spigotmc.org/resources/hideitem-hide-players-1-8-1-15.70313/)  

## API

### Import with maven
To import HideItem, simply add the following code to your pom.xml
Replace {VERSION} with the version listed at the top of this page.

```
<repositories>
    <repository>
        <id>hideitem</id>
        <url>https://repo.vomarek.com/repository/hideitem/</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>com.vomarek.hideitem</groupId>
        <artifactId>HideItem</artifactId>
        <version>{VERSION}</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Methods

```
package com.vomarek.hideitem;

public class HideItem extends JavaPlugin {

    /**
     * Using this method you can set visibility of other players for player.<br>
     * Player will see vanished players if has hideitem.seevanished permission.<br>
     * No message is sent to player!
     *
     * @param player who to set visibility of others to
     * @param hidden should player have hidden players?
     */

    public static void setHiddenState(Player player, Boolean hidden);
    
    /**
     * Using this method you can hide players for specific player.<br>
     * No message is sent to player!
     *
     * @param player Player who you want to hide others to
     */
    public static void hideFor(Player player);
    
    /**
     * Using this method you can show players for specific player.<br>
     * Player will see vanished players if has hideitem.seevanished permission.<br>
     * No message is sent to player!
     *
     * @param player Player who you want to show others to
     */
    public static void showFor(Player player);
}
```

## Support
If you need help with HideItem you can join my [discord support server](https://discord.gg/UjQJW5Z)