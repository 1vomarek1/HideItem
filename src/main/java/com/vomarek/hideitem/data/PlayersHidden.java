package com.vomarek.hideitem.data;

import org.bstats.bukkit.Metrics;

import java.util.concurrent.Callable;

public class PlayersHidden {
    private Metrics metrics;
    private Integer count;

    public PlayersHidden(Metrics metrics) {
        this.metrics = metrics;
        this.count = 0;

        metrics.addCustomChart(new Metrics.SingleLineChart("playersHidden", new Callable<Integer>() {
            @Override
            public Integer call() {
                return count;
            }
        }));
    }

    public void add() {
        count++;
    }
}
