package xyz.srnyx.criticalcolors;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.AnnoyingPAPIExpansion;


public class CriticalPlaceholders extends AnnoyingPAPIExpansion {
    @NotNull private final CriticalColors plugin;

    public CriticalPlaceholders(@NotNull CriticalColors plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public CriticalColors getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getIdentifier() {
        return "critical";
    }

    @Override @Nullable
    public String onPlaceholderRequest(@Nullable Player player, @NotNull String identifier) {
        switch (identifier) {
            case "color": return plugin.data.getColor().map(color -> color.color).orElse("N/A");
            case "rotate": return String.valueOf(plugin.data.getRotate());
            case "bossbar": return String.valueOf(plugin.data.getBossbar());
            default: return null;
        }
    }
}
