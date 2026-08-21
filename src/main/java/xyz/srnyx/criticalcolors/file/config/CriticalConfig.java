package xyz.srnyx.criticalcolors.file.config;

import com.cryptomorin.xseries.XSound;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.commons.duration.DurationSpec;
import eu.okaeri.validator.annotation.NotNull;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;
import xyz.srnyx.annoyingapi.file.PlayableSound;
import xyz.srnyx.annoyingapi.file.okaeri.RootConfig;
import xyz.srnyx.annoyingapi.file.okaeri.SubConfig;
import xyz.srnyx.annoyingapi.stats.Stat;
import xyz.srnyx.criticalcolors.CriticalColors;
import xyz.srnyx.criticalcolors.file.CriticalColor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


@Header("DOCUMENTATION: https://annoying-api.srnyx.com/wiki/File-objects")
public class CriticalConfig extends RootConfig {
    @org.jetbrains.annotations.NotNull private static final String[] DEFAULT_COLORS = {"blue", "brown", "gray", "green", "red"};

    @Comment
    @Comment
    @Comment("Whether the default color files (blue, brown, gray, green, red) should be generated")
    @Stat
    public boolean default_colors = true;

    @Comment
    @Comment("The amount of damage to deal to the player if they step on a colored block")
    @Comment("Put \"kill\" to completely kill the player")
    @Stat
    @NotNull public Damage damage = new Damage();

    @Comment
    @NotNull public Rotate rotate = new Rotate(this);


    @org.jetbrains.annotations.NotNull private transient final CriticalColors plugin;

    public CriticalConfig(@org.jetbrains.annotations.NotNull CriticalColors plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad() {
        // Default colors
        if (default_colors) for (final String color : DEFAULT_COLORS) {
            final AnnoyingResource resource = new AnnoyingResource(plugin, "colors/" + color + ".yml", CriticalColor.FILE_OPTIONS);
            if (!resource.file.exists()) resource.create();
        }
    }

    public static class Rotate extends SubConfig<CriticalConfig, CriticalConfig> {
        public Rotate(@org.jetbrains.annotations.NotNull CriticalConfig parent) {
            super(parent);
        }

        @Comment("The time between each rotation")
        @DurationSpec(fallbackUnit = ChronoUnit.SECONDS) @Stat
        @NotNull public Duration time = Duration.ofMinutes(1);

        @Comment
        @Comment("The time the players are given before the color is changed")
        @Comment("This HAS to be less than the time above (unless time is 0, then it doesn't matter)")
        @DurationSpec(fallbackUnit = ChronoUnit.SECONDS) @Stat
        @NotNull public Duration delay = Duration.ofSeconds(5);

        @Comment
        @NotNull public Sounds sounds = new Sounds(this);

        public static class Sounds extends SubConfig<CriticalConfig, Rotate> {
            public Sounds(@org.jetbrains.annotations.NotNull Rotate parent) {
                super(parent);
            }

            @NotNull public Delay delay = new Delay(this);

            @Comment
            @NotNull public Set set = new Set(this);

            public static class Delay extends SubConfig<CriticalConfig, Sounds> {
                public Delay(@org.jetbrains.annotations.NotNull Sounds parent) {
                    super(parent);
                }

                @Comment("Whether a sound should be played every DELAY second")
                @Stat
                public boolean enabled = true;

                @Comment
                @Comment("SOUND (see documentation)")
                @Stat
                @NotNull public PlayableSound sound = new PlayableSound(XSound.ENTITY_EXPERIENCE_ORB_PICKUP, XSound.Category.MASTER, 0.5f, 0.5f);
            }

            public static class Set extends SubConfig<CriticalConfig, Sounds> {
                public Set(@org.jetbrains.annotations.NotNull Sounds parent) {
                    super(parent);
                }

                @Comment("Whether a sound should be played when the color is set")
                @Stat
                public boolean enabled = true;

                @Comment
                @Comment("SOUND (see documentation)")
                @Stat
                @NotNull public PlayableSound sound = new PlayableSound(XSound.ENTITY_EXPERIENCE_ORB_PICKUP, XSound.Category.MASTER, 0.5f, 1f);
            }
        }
    }
}
