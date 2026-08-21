package xyz.srnyx.criticalcolors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.ServerSoftware;
import xyz.srnyx.annoyingapi.libs.javautilities.FileUtility;
import xyz.srnyx.annoyingapi.message.BroadcastType;
import xyz.srnyx.annoyingapi.message.DefaultReplaceType;
import xyz.srnyx.annoyingapi.utility.DurationUtility;
import xyz.srnyx.criticalcolors.commands.ColorbarCmd;
import xyz.srnyx.criticalcolors.file.CriticalColor;
import xyz.srnyx.criticalcolors.file.config.CriticalConfig;
import xyz.srnyx.criticalcolors.file.CriticalData;
import xyz.srnyx.criticalcolors.file.config.serdes.DamageSerializer;
import xyz.srnyx.criticalcolors.messages.CCMessagesProvider;
import xyz.srnyx.criticalcolors.stats.FastStats;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

import static xyz.srnyx.annoyingapi.reflection.org.bukkit.RefNamespacedKey.NAMESPACED_KEY_CONSTRUCTOR;
import static xyz.srnyx.criticalcolors.reflection.org.bukkit.RefBukkit.*;
import static xyz.srnyx.criticalcolors.reflection.org.bukkit.boss.RefBarColor.*;
import static xyz.srnyx.criticalcolors.reflection.org.bukkit.boss.RefBarFlag.BAR_FLAG_ARRAY_NEW;
import static xyz.srnyx.criticalcolors.reflection.org.bukkit.boss.RefBarStyle.BAR_STYLE_VALUE_SOLID;
import static xyz.srnyx.criticalcolors.reflection.org.bukkit.boss.RefBossBar.*;


public class CriticalColors extends AnnoyingPlugin {
    @NotNull private static final Random RANDOM = new Random();

    public CriticalConfig config;
    public CriticalData data;
    @NotNull public final Set<CriticalColor> colors = new HashSet<>();
    @Nullable public Object bossBar;
    @Nullable public BukkitTask rotateRunnable;
    @Nullable public BukkitTask delayRunnable;

    public CriticalColors() {
        options
                .statsOptions(statsOptions -> statsOptions
                        .bStats(bStatsOptions -> bStatsOptions.id(18858))
                        .fastStats(fastStatsOptions -> fastStatsOptions.loader(FastStats.class)))
                .dataOptions(dataOptions -> dataOptions
                        .table(CriticalData.TABLE, CriticalData.COL_COLOR, CriticalData.COL_ROTATE, CriticalData.COL_BOSSBAR))
                .registrationOptions.papiExpansionToRegister(() -> new CriticalPlaceholders(this));

        // Don't register bar command if server version lower than 1.9 TODO does mc-version work?
        if (ServerSoftware.MINECRAFT_VERSION == null || ServerSoftware.MINECRAFT_VERSION.isLowerThan("1.9.0")) {
            options.registrationOptions.automaticRegistration.ignoredClasses(ColorbarCmd.class);
        }
    }

    @Override @NotNull
    public CCMessagesProvider getMessages() {
        return (CCMessagesProvider) super.getMessages();
    }

    @Override
    public void load() {
        config = configLoader.build(builder -> builder
                .config(new CriticalConfig(this))
                .configure(configure -> configure.serdes(new DamageSerializer())));
    }

    @Override
    public void enable() {
        // Create bossbar
        if (bossBar == null) try {
            if (CREATE_BOSS_BAR_METHOD_5 != null && NAMESPACED_KEY_CONSTRUCTOR != null) {
                bossBar = CREATE_BOSS_BAR_METHOD_5.invoke(Bukkit.class, NAMESPACED_KEY_CONSTRUCTOR.newInstance(this, "cc_bar"), "N/A", BAR_COLOR_VALUE_WHITE, BAR_STYLE_VALUE_SOLID, BAR_FLAG_ARRAY_NEW);
            } else if (CREATE_BOSS_BAR_METHOD_4 != null) {
                bossBar = CREATE_BOSS_BAR_METHOD_4.invoke(Bukkit.class, "N/A", BAR_COLOR_VALUE_WHITE, BAR_STYLE_VALUE_SOLID, BAR_FLAG_ARRAY_NEW);
            }
        } catch (final IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log(Level.WARNING, "Failed to create boss bar", e);
        }

        // colors, data, bar, and rotating
        FileUtility.getFileNames(new File(getDataFolder(), "colors"), "yml").forEach(name -> colors.add(new CriticalColor(this, name)));
        data = new CriticalData(this);
        data.convertOldData();
        updateBar();
        toggleRotating();
    }

    @Override
    public void reload() {
        config.reload();

        // Remove boss bar
        if (bossBar != null) {
            setBarVisibility(false);
            if (REMOVE_BOSS_BAR_METHOD != null && NAMESPACED_KEY_CONSTRUCTOR != null) try {
                REMOVE_BOSS_BAR_METHOD.invoke(Bukkit.class, NAMESPACED_KEY_CONSTRUCTOR.newInstance(this, "cc_bar"));
            } catch (final IllegalAccessException | InvocationTargetException | InstantiationException e) {
                log(Level.WARNING, "Failed to remove boss bar", e);
            }
            bossBar = null;
        }

        stopRotating();
        enable();
    }

    public void toggleRotating() {
        // Check if enabled
        if (!data.getRotate()) {
            stopRotating();
            return;
        }

        // Check if delay longer than time
        if (config.rotate.delay.toMillis() >= config.rotate.time.toMillis()) {
            log(Level.WARNING, "Delay is longer than or equal to time, disabling rotation");
            stopRotating();
            return;
        }

        updateBar();

        if (rotateRunnable != null) rotateRunnable.cancel();
        rotateRunnable = new BukkitRunnable() {
            public void run() {
                if (!data.getRotate()) {
                    stopRotating();
                    return;
                }

                // Get next color
                final CriticalColor currentColor = data.getColor().orElse(null);
                final CriticalColor newColor = colors.stream()
                        .filter(streamColor -> !streamColor.equals(currentColor))
                        .skip(RANDOM.nextInt(colors.size() - 1))
                        .findFirst()
                        .orElse(null);
                if (newColor == null) return;

                // Start delay countdown
                final AtomicLong delay = new AtomicLong(config.rotate.delay.toSeconds());
                delayRunnable = new BukkitRunnable() {
                    public void run() {
                        final long delayValue = delay.get();

                        // Set new color
                        if (delayValue == 0) {
                            data.setColor(newColor);
                            cancel();
                            delayRunnable = null;

                            // Send set message & play sound
                            getMessages().get().rotate.set.newMessage()
                                    .replace("%chatcolor%", newColor.chatColor.toString())
                                    .replace("%color%", newColor.color)
                                    .broadcast(BroadcastType.CHAT);
                            if (config.rotate.sounds.set.enabled) Bukkit.getOnlinePlayers().forEach(config.rotate.sounds.set.sound::play);
                            return;
                        }
                        delay.set(delayValue - 1);

                        // Send delay message & play sound
                        getMessages().get().rotate.delay.newMessage()
                                .replace("%delay%", delayValue * 1000, DefaultReplaceType.TIME)
                                .replace("%chatcolor%", newColor.chatColor.toString())
                                .replace("%color%", newColor.color)
                                .broadcast(BroadcastType.FULL_TITLE, 0, 25, 0);
                        if (config.rotate.sounds.delay.enabled) Bukkit.getOnlinePlayers().forEach(config.rotate.sounds.delay.sound::play);

                        // Set bar progress
                        if (bossBar != null && BOSS_BAR_SET_PROGRESS_METHOD != null) try {
                            BOSS_BAR_SET_PROGRESS_METHOD.invoke(bossBar, (double) delayValue / config.rotate.delay.toSeconds());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            log(Level.WARNING, "Failed to set boss bar progress", e);
                        }
                    }
                }.runTaskTimer(CriticalColors.this, 0, 20);
            }
        }.runTaskTimer(this, DurationUtility.durationToTicks(config.rotate.time), DurationUtility.durationToTicks(config.rotate.time));
    }

    private void stopRotating() {
        if (rotateRunnable != null) {
            rotateRunnable.cancel();
            rotateRunnable = null;
        }
        if (delayRunnable != null) {
            delayRunnable.cancel();
            delayRunnable = null;
        }
        updateBar();
    }

    public void updateBar() {
        if (bossBar == null) return;
        if (!data.getBossbar()) {
            setBarVisibility(false);
            return;
        }
        final CriticalColor color = data.getColor().orElse(null);
        if (color == null) {
            setBarVisibility(false);
            return;
        }
        setBarVisibility(true);

        // Set title, color & progress
        try {
            if (BOSS_BAR_SET_TITLE_METHOD != null) BOSS_BAR_SET_TITLE_METHOD.invoke(bossBar, getMessages().get().bossbar.newMessage()
                    .replace("%chatcolor%", color.chatColor.toString())
                    .replace("%color%", color.color)
                    .toString());
            if (BOSS_BAR_SET_COLOR_METHOD != null && color.barColor != null) BOSS_BAR_SET_COLOR_METHOD.invoke(bossBar, color.barColor);
            if (BOSS_BAR_SET_PROGRESS_METHOD != null) BOSS_BAR_SET_PROGRESS_METHOD.invoke(bossBar, 1.0);
        } catch (final InvocationTargetException | IllegalAccessException e) {
            log(Level.WARNING, "Failed to update boss bar", e);
        }
    }

    @Nullable
    public CriticalColor getColor(@Nullable String name) {
        if (name == null) return null;
        for (final CriticalColor color : colors) if (color.color.equalsIgnoreCase(name)) return color;
        return null;
    }

    private void setBarVisibility(boolean enabled) {
        if (bossBar != null && BOSS_BAR_SET_VISIBLE_METHOD != null) try {
            if (!enabled) {
                BOSS_BAR_SET_VISIBLE_METHOD.invoke(bossBar, false);
                return;
            }
            if (BOSS_BAR_ADD_PLAYER_METHOD != null) for (final Player player : Bukkit.getOnlinePlayers()) BOSS_BAR_ADD_PLAYER_METHOD.invoke(bossBar, player);
            BOSS_BAR_SET_VISIBLE_METHOD.invoke(bossBar, true);
        } catch (final InvocationTargetException | IllegalAccessException e) {
            log(Level.WARNING, "Failed to set boss bar visibility", e);
        }
    }
}
