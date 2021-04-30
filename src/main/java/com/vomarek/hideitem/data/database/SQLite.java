package com.vomarek.hideitem.data.database;

import com.vomarek.hideitem.HideItem;

import java.sql.*;

public class SQLite implements Database {
    private HideItem plugin;

    private Connection conn;

    public SQLite (final HideItem plugin) {

        this.plugin = plugin;

        createConnection();

    }

    private void createConnection () {
        try {

            conn = DriverManager.getConnection("jdbc:sqlite:"+plugin.getDataFolder()+"/data.db");

            final Statement stmt = conn.createStatement();

            final String sql = "CREATE TABLE IF NOT EXISTS HideItem (player VARCHAR(36) NOT NULL, state VARCHAR(16) , PRIMARY KEY ( player ))";
            stmt.executeUpdate(sql);

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setState(String uuid, String state) {
        try {
            if (conn.isClosed()) createConnection();

            final PreparedStatement stmt = conn.prepareStatement("INSERT OR REPLACE INTO HideItem (player, state) VALUES (?, ?)");

            stmt.setString(1, uuid);
            stmt.setString(2, state);

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

            final PreparedStatement stmt = conn.prepareStatement("SELECT * FROM HideItem WHERE player=?");
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
