package com.vomarek.hideitem.data.database;

import com.vomarek.hideitem.HideItem;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class MySQL implements Database {
    private Connection conn;

    private final String HOST;
    private final Integer PORT;
    private final String USER;
    private final String PASSWORD;
    private final String DATABASE;
    private final String TABLE;

    public MySQL(@NotNull final HideItem plugin, @NotNull final YamlConfiguration config) {

        HOST = config.getString("mysql.host", "localhost");
        PORT = config.getInt("mysql.port", 3306);
        DATABASE = config.getString("mysql.database", "HideItem");
        TABLE = config.getString("mysql.table", "HideItem");
        USER = config.getString("mysql.user", "root");
        PASSWORD = config.getString("mysql.password", "");


        createConnection();
    }

    private void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://"+HOST+":"+PORT+"/?useUnicode=true&characterEncoding=utf8&useSSL=false&verifyServerCertificate=false", USER, PASSWORD);

            final Statement stmt = conn.createStatement();

            String sql = "CREATE DATABASE IF NOT EXISTS "+DATABASE;
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS "+DATABASE+"."+TABLE+" (player VARCHAR(16) NOT NULL, state VARCHAR(16) , PRIMARY KEY ( player ))";
            stmt.executeUpdate(sql);

            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setState(String uuid, String state) {

        try {
            if (conn.isClosed()) createConnection();

            final PreparedStatement stmt = conn.prepareStatement("INSERT INTO "+DATABASE+"."+TABLE+" (player, state) VALUES (?, ?) ON DUPLICATE KEY UPDATE state=?");
            stmt.setString(1, uuid);
            stmt.setString(2, state);
            stmt.setString(3, state);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getState(String uuid) {
        try {
            if (conn.isClosed()) createConnection();

            final PreparedStatement stmt = conn.prepareStatement("SELECT * FROM "+DATABASE+"."+TABLE+" WHERE player=?");
            stmt.setString(1, uuid);

            if (!stmt.execute()) return null;

            ResultSet results = stmt.getResultSet();

            while (results.next()) {
                if (results.getString("state") == null) continue;

                return results.getString("state");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close () {
        try {
            if (!conn.isClosed()) conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
