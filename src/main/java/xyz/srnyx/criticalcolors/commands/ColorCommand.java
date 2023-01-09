package xyz.srnyx.criticalcolors.commands;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingMessage;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;

import xyz.srnyx.criticalcolors.CriticalColors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class ColorCommand implements AnnoyingCommand {
    @NotNull private final CriticalColors plugin;
    @NotNull private final Set<String> colors;

    @Contract(pure = true)
    public ColorCommand(@NotNull final CriticalColors plugin) {
        this.plugin = plugin;
        this.colors = plugin.section.getKeys(false);
    }

    @Override @NotNull
    public AnnoyingPlugin getPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "criticalcolors.color";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        final String[] args = sender.getArgs();

        // No arguments
        if (args.length == 0) {
            if (plugin.color == null) {
                new AnnoyingMessage(plugin, "command.get.none").send(sender);
                return;
            }
            new AnnoyingMessage(plugin, "command.get.message")
                    .replace("%color%", plugin.color)
                    .send(sender);
            return;
        }

        // <color>
        String color = args[0];
        if (color.equalsIgnoreCase("none")) color = null;
        plugin.setColor(color);
        plugin.data.set("color", color, true);

        // Message
        if (color == null) {
            new AnnoyingMessage(plugin, "command.set.none").send(sender);
            return;
        }
        new AnnoyingMessage(plugin, "command.set.message")
                .replace("%color%", color)
                .send(sender);
    }

    @Override @NotNull
    public Collection<String> onTabComplete(@NotNull AnnoyingSender sender) {
        final HashSet<String> suggestions = new HashSet<>(colors);
        suggestions.add("none");
        return suggestions;
    }
}
