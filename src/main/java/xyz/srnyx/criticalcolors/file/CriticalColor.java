package xyz.srnyx.criticalcolors.file;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.file.AnnoyingResource;
import xyz.srnyx.annoyingapi.utility.ReflectionUtility;

import xyz.srnyx.criticalcolors.CriticalColors;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static xyz.srnyx.criticalcolors.reflection.org.bukkit.boss.RefBarColor.*;


public class CriticalColor {
    @NotNull public static final AnnoyingResource.Options FILE_OPTIONS = new AnnoyingResource.Options().createDefaultFile(false).canBeEmpty(false);

    @NotNull public final String color;
    @NotNull public final Set<Material> materials;
    @NotNull public final ChatColor chatColor;
    @Nullable public final Object barColor;

    public CriticalColor(@NotNull CriticalColors plugin, @NotNull String color) {
        this.color = color;
        final AnnoyingResource resource = new AnnoyingResource(plugin, "colors/" + color + ".yml", FILE_OPTIONS);
        materials = resource.getStringList("blocks").stream()
                .map(Material::matchMaterial)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // colors
        final ConfigurationSection colors = resource.getConfigurationSection("colors");
        String chatColorString = null;
        String barColorString = null;
        if (colors != null) {
            chatColorString = colors.getString("chat");
            barColorString = colors.getString("bar");
        }

        // chatColor
        ChatColor newChatColor = null;
        if (chatColorString != null) { // From config
            newChatColor = getChatColor(chatColorString.toUpperCase());
            if (newChatColor == null) resource.log(Level.WARNING, "colors.chat", "&eInvalid chat color: &6" + chatColorString + "&e");
        }
        if (newChatColor == null) newChatColor = getChatColor(color.toUpperCase()); // From color name
        if (newChatColor == null) newChatColor = ChatColor.WHITE; // Default
        chatColor = newChatColor;

        // barColor
        Object newBarColor = null;
        if (barColorString != null) { // From config
            newBarColor = getBarColor(barColorString);
            if (newBarColor == null) resource.log(Level.WARNING, "colors.bar", "&eInvalid bar color: &6" + barColorString + "&e");
        }
        if (newBarColor == null) newBarColor = getBarColor(color.toUpperCase()); // From color name
        if (newBarColor == null) newBarColor = BAR_COLOR_VALUE_WHITE; // Default
        barColor = newBarColor;
    }

    @Nullable
    private ChatColor getChatColor(@NotNull String name) {
        try {
            return ChatColor.valueOf(name.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    @Nullable
    private Object getBarColor(@NotNull String name) {
        return ReflectionUtility.getEnumValue(1, 9, 0, BAR_COLOR_ENUM, name.toUpperCase());
    }
}
