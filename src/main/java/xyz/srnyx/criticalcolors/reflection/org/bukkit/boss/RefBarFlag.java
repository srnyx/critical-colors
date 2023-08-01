package xyz.srnyx.criticalcolors.reflection.org.bukkit.boss;

import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.utility.ReflectionUtility;


/**
 * 1.9+ org.bukkit.boss.BarFlag
 */
public enum RefBarFlag {;
    /**
     * 1.9+ org.bukkit.boss.BarFlag
     */
    @Nullable public static final Class<? extends Enum> BAR_FLAG_ENUM = ReflectionUtility.getEnum(1, 9, 0, "org.bukkit.boss.BarFlag");

    /**
     * 1.9+ org.bukkit.boss.BarFlag[]
     */
    @Nullable public static final Class<?> BAR_FLAG_ARRAY = ReflectionUtility.getClass(1, 9, 0, "[Lorg.bukkit.boss.BarFlag;");

    /**
     * 1.9+ new org.bukkit.boss.BarFlag[]
     */
    @Nullable public static final Object BAR_FLAG_ARRAY_NEW = ReflectionUtility.createArray(BAR_FLAG_ENUM, 0);
}
