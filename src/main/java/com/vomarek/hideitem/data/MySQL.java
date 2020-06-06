package com.vomarek.hideitem.data;

import org.bukkit.entity.Player;

import java.sql.*;

public class MySQL {
    private String host;
    private Integer port;
    private String database;
    private String table;
    private String user;
    private String password;

    public MySQL(String host, Integer port, String database, String table, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.table = table;
        this.user = user;
        this.password = password;

        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            final Connection conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/?characterEncoding=latin1&useConfigs=maxPerformance", user, password);

            final Statement stmt = conn.createStatement();

            String sql = "CREATE DATABASE IF NOT EXISTS "+database;
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS "+database+"."+table+" (player VARCHAR(16) NOT NULL, state VARCHAR(16) , PRIMARY KEY ( player ))";
            stmt.executeUpdate(sql);

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void setState(Player player, String state) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            final Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/?characterEncoding=latin1&useConfigs=maxPerformance", user, password);

            final PreparedStatement stmt = conn.prepareStatement("INSERT INTO "+database+"."+table+" (player, state) VALUES (?, ?) ON DUPLICATE KEY UPDATE state=?");
            stmt.setString(1, player.getName());
            stmt.setString(2, state);
            stmt.setString(3, state);

            stmt.execute();

            conn.close();
        } catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String getState(Player player) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            final Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/?characterEncoding=latin1&useConfigs=maxPerformance", user, password);

            final PreparedStatement stmt = conn.prepareStatement("SELECT * FROM "+database+"."+table+" WHERE player=?");
            stmt.setString(1, player.getName());

            if (!stmt.execute()) return null;

            ResultSet results = stmt.getResultSet();

            while (results.next()) {
                if (results.getString("state") == null) continue;

                return results.getString("state");
            }

            conn.close();
        } catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
