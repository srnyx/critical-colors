package xyz.srnyx.criticalcolors.reflection.org.bukkit;

import org.bukkit.Bukkit;

import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.reflection.org.bukkit.RefNamespacedKey;
import xyz.srnyx.annoyingapi.utility.ReflectionUtility;

import xyz.srnyx.criticalcolors.reflection.org.bukkit.boss.RefBarColor;
import xyz.srnyx.criticalcolors.reflection.org.bukkit.boss.RefBarFlag;
import xyz.srnyx.criticalcolors.reflection.org.bukkit.boss.RefBarStyle;

import java.lang.reflect.Method;


/**
 * org.bukkit.Bukkit
 */
public class RefBukkit {
    /**
     * 1.9+ org.bukkit.Bukkit#createBossBar(String, org.bukkit.boss.BarColor, org.bukkit.boss.BarStyle, org.bukkit.boss.BarFlag...)
     */
    @Nullable public static final Method CREATE_BOSS_BAR_METHOD_4 = ReflectionUtility.getMethod(1, 9, 0, Bukkit.class, "createBossBar", String.class, RefBarColor.BAR_COLOR_ENUM, RefBarStyle.BAR_STYLE_ENUM, RefBarFlag.BAR_FLAG_ARRAY);

    /**
     * 1.13.2+ org.bukkit.Bukkit#createBossBar(org.bukkit.NamespacedKey, String, org.bukkit.boss.BarColor, org.bukkit.boss.BarStyle, org.bukkit.boss.BarFlag...)
     */
    @Nullable public static final Method CREATE_BOSS_BAR_METHOD_5 = ReflectionUtility.getMethod(1, 13, 2, Bukkit.class, "createBossBar", RefNamespacedKey.NAMESPACED_KEY_CLASS, String.class, RefBarColor.BAR_COLOR_ENUM, RefBarStyle.BAR_STYLE_ENUM, RefBarFlag.BAR_FLAG_ARRAY);

    /**
     * 1.13.2+ org.bukkit.Bukkit#removeBossBar(org.bukkit.NamespacedKey)
     */
    @Nullable public static final Method REMOVE_BOSS_BAR_METHOD = ReflectionUtility.getMethod(1, 13, 2, Bukkit.class, "removeBossBar", RefNamespacedKey.NAMESPACED_KEY_CLASS);
}
