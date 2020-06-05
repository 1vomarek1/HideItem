# HideItem
**by 1vomarek1**    
HideItem is spigot plugin used to give players option to toggle visibility of other players.  
This plugin was originaly created by **qKing12**.

## API

### Import with maven
To import HideItem, simply add the following code to your pom.xml
Replace {VERSION} with the version listed at the top of this page.

```xml
<repositories>
    <repository>
        <id>hideitem</id>
        <url>https://repo.vomarek.com/repository/HideItem/</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>com.vomarek.HideItem</groupId>
        <artifactId>HideItem</artifactId>
        <version>{VERSION}</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Methods

```

HideItem.setHiddenState(Player player, Boolean hidden);

HideItem.hideFor(Player player, Boolean hidden);

HideItem.showFor(Player player, Boolean hidden);
```

## Support
If you need help with HideItem you can join my [discord support server](https://discord.gg/UjQJW5Z)