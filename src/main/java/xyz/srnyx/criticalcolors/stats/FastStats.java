package xyz.srnyx.criticalcolors.stats;

import dev.faststats.Metrics;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.stats.loader.FastStatsLoader;
import xyz.srnyx.criticalcolors.CriticalColors;


public class FastStats extends FastStatsLoader {
    @NotNull private final CriticalColors plugin;

    public FastStats(@NotNull CriticalColors plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public CriticalColors getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getId() {
        return "4b9be1f8078e48b08dfa5553cd5224ff";
    }

    @Override
    public void mutateMetricsFactory(@NotNull Metrics.Factory factory) {
        factory.addMetric(config("config", () -> plugin.config));
    }
}
