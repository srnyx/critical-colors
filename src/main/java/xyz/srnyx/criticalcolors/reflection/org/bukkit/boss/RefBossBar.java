package xyz.srnyx.criticalcolors.reflection.org.bukkit.boss;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.utility.ReflectionUtility;

import java.lang.reflect.Method;


/**
 * 1.9+ org.bukkit.boss.BossBar
 */
public class RefBossBar {
    /**
     * 1.9+ org.bukkit.boss.BossBar
     */
    @Nullable public static final Class<?> BOSS_BAR_CLASS = ReflectionUtility.getClass(1, 9, 0, "org.bukkit.boss.BossBar");

    /**
     * 1.9+ org.bukkit.boss.BossBar#addPlayer(org.bukkit.entity.Player)
     */
    @Nullable public static final Method BOSS_BAR_ADD_PLAYER_METHOD = ReflectionUtility.getMethod(1, 9, 0, BOSS_BAR_CLASS, "addPlayer", Player.class);

    /**
     * 1.9+ org.bukkit.boss.BossBar#setColor(org.bukkit.boss.BarColor)
     */
    @Nullable public static final Method BOSS_BAR_SET_COLOR_METHOD = ReflectionUtility.getMethod(1, 9, 0, BOSS_BAR_CLASS, "setColor", RefBarColor.BAR_COLOR_ENUM);

    /**
     * 1.9+ org.bukkit.boss.BossBar#setProgress(double)
     */
    @Nullable public static final Method BOSS_BAR_SET_PROGRESS_METHOD = ReflectionUtility.getMethod(1, 9, 0, BOSS_BAR_CLASS, "setProgress", double.class);

    /**
     * 1.9+ org.bukkit.boss.BossBar#setTitle(String)
     */
    @Nullable public static final Method BOSS_BAR_SET_TITLE_METHOD = ReflectionUtility.getMethod(1, 9, 0, BOSS_BAR_CLASS, "setTitle", String.class);

    /**
     * 1.9+ org.bukkit.boss.BossBar#setVisible(boolean)
     */
    @Nullable public static final Method BOSS_BAR_SET_VISIBLE_METHOD = ReflectionUtility.getMethod(1, 9, 0, BOSS_BAR_CLASS, "setVisible", boolean.class);
}
