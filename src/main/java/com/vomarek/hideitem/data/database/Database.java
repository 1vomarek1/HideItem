package com.vomarek.hideitem.data.database;


import org.bukkit.entity.Player;

public interface Database {

    void setState(String uuid, String state);

    String getState(String uuid);
}
