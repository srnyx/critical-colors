package xyz.srnyx.criticalcolors.file;

import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.file.AnnoyingResource;
import xyz.srnyx.annoyingapi.file.PlayableSound;

import xyz.srnyx.criticalcolors.CriticalColors;


public class CriticalConfig {
    @NotNull private static final String[] DEFAULT_COLORS = {"blue", "brown", "gray", "green", "red"};

    @Nullable public final Double damage;
    @Nullable public final Integer rotateTime;
    @Nullable public final Integer rotateDelay;
    @Nullable public final PlayableSound rotateSoundDelay;
    @Nullable public final PlayableSound rotateSoundSet;

    public CriticalConfig(@NotNull CriticalColors plugin) {
        final AnnoyingResource config = new AnnoyingResource(plugin, "config.yml");

        // Default colors
        if (config.getBoolean("default-colors", true)) for (final String color : DEFAULT_COLORS) {
            final AnnoyingResource resource = new AnnoyingResource(plugin, "colors/" + color + ".yml", CriticalColor.FILE_OPTIONS);
            if (!resource.file.exists()) resource.create();
        }

        // damage
        final double damageConfig = config.getDouble("damage");
        damage = damageConfig == 0 ? null : damageConfig;

        // rotateTime & rotateDelay
        final ConfigurationSection rotate = config.getConfigurationSection("rotate");
        final boolean hasRotate = rotate != null;
        Integer newRotateTime = hasRotate ? rotate.getInt("time") : 0;
        Integer newRotateDelay = hasRotate ? rotate.getInt("delay") : 0;
        if (newRotateDelay > newRotateTime) {
            newRotateTime = null;
            newRotateDelay = null;
        }
        rotateTime = newRotateTime;
        rotateDelay = newRotateDelay;

        // rotateSoundDelay & rotateSoundSet
        rotateSoundDelay = config.getPlayableSound("rotate.sounds.delay").orElse(null);
        rotateSoundSet = config.getPlayableSound("rotate.sounds.set").orElse(null);
    }
}
