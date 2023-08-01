package xyz.srnyx.criticalcolors.reflection.org.bukkit.boss;

import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.utility.ReflectionUtility;


/**
 * 1.9+ org.bukkit.boss.BarColor
 */
public enum RefBarColor {;
    /**
     * 1.9+ org.bukkit.boss.BarColor
     */
    @Nullable public static final Class<? extends Enum> BAR_COLOR_ENUM = ReflectionUtility.getEnum(1, 9, 0, "org.bukkit.boss.BarColor");

    /**
     * 1.9+ org.bukkit.boss.BarColor#WHITE
     */
    @Nullable public static final Object BAR_COLOR_VALUE_WHITE = ReflectionUtility.getEnumValue(1, 9, 0, BAR_COLOR_ENUM, "WHITE");
}
