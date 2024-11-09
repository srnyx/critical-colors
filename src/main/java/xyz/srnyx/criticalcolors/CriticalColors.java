package xyz.srnyx.criticalcolors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;
import xyz.srnyx.annoyingapi.libs.javautilities.FileUtility;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.message.BroadcastType;
import xyz.srnyx.annoyingapi.message.DefaultReplaceType;

import xyz.srnyx.criticalcolors.commands.ColorbarCmd;
import xyz.srnyx.criticalcolors.file.CriticalColor;
import xyz.srnyx.criticalcolors.file.CriticalConfig;
import xyz.srnyx.criticalcolors.file.CriticalData;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("critical-colors"),
                        PluginPlatform.hangar(this, "srnyx"),
                        PluginPlatform.spigot("107312")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(18858))
                .dataOptions(dataOptions -> dataOptions
                        .enabled(true)
                        .table(CriticalData.TABLE, CriticalData.COL_COLOR, CriticalData.COL_ROTATE, CriticalData.COL_BOSSBAR))
                .registrationOptions
                .automaticRegistration(automaticRegistration -> automaticRegistration.packages(
                        "xyz.srnyx.criticalcolors.commands",
                        "xyz.srnyx.criticalcolors.listeners"))
                .papiExpansionToRegister(() -> new CriticalPlaceholders(this));
        if (MINECRAFT_VERSION.isLessThan(1, 9, 0)) options.registrationOptions.automaticRegistration.ignoredClasses(ColorbarCmd.class);
    }

    @Override
    public void enable() {
        // bossBar
        if (bossBar == null) try {
            if (CREATE_BOSS_BAR_METHOD_5 != null && NAMESPACED_KEY_CONSTRUCTOR != null) {
                bossBar = CREATE_BOSS_BAR_METHOD_5.invoke(Bukkit.class, NAMESPACED_KEY_CONSTRUCTOR.newInstance(this, "cc_bar"), "N/A", BAR_COLOR_VALUE_WHITE, BAR_STYLE_VALUE_SOLID, BAR_FLAG_ARRAY_NEW);
            } else if (CREATE_BOSS_BAR_METHOD_4 != null) {
                bossBar = CREATE_BOSS_BAR_METHOD_4.invoke(Bukkit.class, "N/A", BAR_COLOR_VALUE_WHITE, BAR_STYLE_VALUE_SOLID, BAR_FLAG_ARRAY_NEW);
            }
        } catch (final IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log(Level.WARNING, "Failed to create boss bar", e);
        }

        // config, colors, & data
        config = new CriticalConfig(this);
        FileUtility.getFileNames(new File(getDataFolder(), "colors"), "yml").forEach(name -> colors.add(new CriticalColor(this, name)));
        data = new CriticalData(this);
        data.convertOldData();
        updateBar();
        toggleRotating();
    }

    @Override
    public void reload() {
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
        final boolean rotate = data.getRotate();
        if (!rotate || config.rotateTime == null || config.rotateDelay == null) {
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
                final AtomicInteger delay = new AtomicInteger(config.rotateDelay);
                delayRunnable = new BukkitRunnable() {
                    public void run() {
                        final int delayValue = delay.get();

                        // Set new color
                        if (delayValue == 0) {
                            data.setColor(newColor);
                            cancel();
                            delayRunnable = null;

                            // Send set message & play sound
                            new AnnoyingMessage(CriticalColors.this, "rotate.set")
                                    .replace("%chatcolor%", newColor.chatColor.toString())
                                    .replace("%color%", newColor.color)
                                    .broadcast(BroadcastType.CHAT);
                            if (config.rotateSoundSet != null)
                                Bukkit.getOnlinePlayers().forEach(config.rotateSoundSet::play);
                            return;
                        }
                        delay.set(delayValue - 1);

                        // Send delay message & play sound
                        new AnnoyingMessage(CriticalColors.this, "rotate.delay")
                                .replace("%delay%", delayValue * 1000, DefaultReplaceType.TIME)
                                .replace("%chatcolor%", newColor.chatColor.toString())
                                .replace("%color%", newColor.color)
                                .broadcast(BroadcastType.FULL_TITLE, 0, 25, 0);
                        if (config.rotateSoundDelay != null) Bukkit.getOnlinePlayers().forEach(config.rotateSoundDelay::play);

                        // Set bar progress
                        if (bossBar != null && BOSS_BAR_SET_PROGRESS_METHOD != null) try {
                            BOSS_BAR_SET_PROGRESS_METHOD.invoke(bossBar, (double) delayValue / config.rotateDelay);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            log(Level.WARNING, "Failed to set boss bar progress", e);
                        }
                    }
                }.runTaskTimer(CriticalColors.this, 0, 20);
            }
        }.runTaskTimer(this, config.rotateTime * 20L, config.rotateTime * 20L);
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
            if (BOSS_BAR_SET_TITLE_METHOD != null) BOSS_BAR_SET_TITLE_METHOD.invoke(bossBar, new AnnoyingMessage(this, "bossbar")
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
        final String nameLower = name.toLowerCase();
        return colors.stream()
                .filter(color -> color.color.toLowerCase().equals(nameLower))
                .findFirst()
                .orElse(null);
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
