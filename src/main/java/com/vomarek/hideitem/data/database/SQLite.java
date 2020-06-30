package com.vomarek.hideitem.data.database;

import com.vomarek.hideitem.HideItem;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class SQLite implements Database {
    private HideItem plugin;

    private Connection conn;

    public SQLite (@NotNull final HideItem plugin) {

        this.plugin = plugin;

        createConnection();

    }

    private void createConnection () {
        try {

            conn = DriverManager.getConnection("jdbc:sqlite:"+plugin.getDataFolder()+"/data.db");

            final Statement stmt = conn.createStatement();

            final String sql = "CREATE TABLE IF NOT EXISTS HideItem (player VARCHAR(16) NOT NULL, state VARCHAR(16) , PRIMARY KEY ( player ))";
            stmt.executeUpdate(sql);

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
