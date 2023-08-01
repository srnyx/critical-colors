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
        // color
        if (identifier.equals("color")) return plugin.data.color == null ? "N/A" : plugin.data.color.color;

        // rotate
        if (identifier.equals("rotate")) return String.valueOf(plugin.data.rotate);

        // bossbar
        if (identifier.equals("bossbar")) return String.valueOf(plugin.data.bossbar);

        return null;
    }
}
