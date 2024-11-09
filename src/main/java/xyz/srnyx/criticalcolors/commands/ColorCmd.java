package xyz.srnyx.criticalcolors.commands;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;

import xyz.srnyx.criticalcolors.CriticalColors;
import xyz.srnyx.criticalcolors.file.CriticalColor;

import java.util.Set;
import java.util.stream.Collectors;


public class ColorCmd extends AnnoyingCommand {
    @NotNull private final CriticalColors plugin;

    public ColorCmd(@NotNull CriticalColors plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public CriticalColors getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "criticalcolors.color";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        // No arguments
        if (sender.args.length == 0) {
            final CriticalColor color = plugin.data.getColor().orElse(null);
            if (color == null) {
                new AnnoyingMessage(plugin, "command.get.none").send(sender);
                return;
            }
            new AnnoyingMessage(plugin, "command.get.message")
                    .replace("%chatcolor%", color.chatColor.toString())
                    .replace("%color%", color.color)
                    .send(sender);
            return;
        }

        // <color>
        final CriticalColor color = sender.getArgument(0, plugin::getColor);
        plugin.data.setColor(color);

        // Message
        if (color == null) {
            new AnnoyingMessage(plugin, "command.set.none").send(sender);
            return;
        }
        new AnnoyingMessage(plugin, "command.set.message")
                .replace("%chatcolor%", color.chatColor.toString())
                .replace("%color%", color.color)
                .send(sender);
    }

    @Override @NotNull
    public Set<String> onTabComplete(@NotNull AnnoyingSender sender) {
        final Set<String> suggestions = plugin.colors.stream()
                .map(color -> color.color)
                .collect(Collectors.toSet());
        suggestions.add("none");
        return suggestions;
    }
}
