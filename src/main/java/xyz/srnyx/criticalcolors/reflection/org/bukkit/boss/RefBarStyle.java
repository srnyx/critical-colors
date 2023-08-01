package xyz.srnyx.criticalcolors.reflection.org.bukkit.boss;

import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.utility.ReflectionUtility;


/**
 * 1.9+ org.bukkit.boss.BarStyle
 */
public enum RefBarStyle {;
    /**
     * 1.9+ org.bukkit.boss.BarStyle
     */
    @Nullable public static final Class<? extends Enum> BAR_STYLE_ENUM = ReflectionUtility.getEnum(1, 9, 0, "org.bukkit.boss.BarStyle");

    /**
     * 1.9+ org.bukkit.boss.BarStyle#SOLID
     */
    @Nullable public static final Object BAR_STYLE_VALUE_SOLID = ReflectionUtility.getEnumValue(1, 9, 0, BAR_STYLE_ENUM, "SOLID");
}
