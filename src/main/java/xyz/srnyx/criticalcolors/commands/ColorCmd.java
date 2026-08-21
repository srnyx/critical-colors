package xyz.srnyx.criticalcolors.commands;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.criticalcolors.CriticalColors;
import xyz.srnyx.criticalcolors.file.CriticalColor;

import java.util.Set;
import java.util.stream.Collectors;


public class ColorCmd extends xyz.srnyx.criticalcolors.commands.generated.ColorCmdGen {
    public ColorCmd(@NotNull CriticalColors plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        // No arguments
        if (sender.args.length == 0) {
            final CriticalColor color = plugin.data.getColor().orElse(null);
            if (color == null) {
                plugin.getMessages().get().command.get.none.newMessage().send(sender);
                return;
            }
            plugin.getMessages().get().command.get.message.newMessage()
                    .replace("%chatcolor%", color.chatColor.toString())
                    .replace("%color%", color.color)
                    .send(sender);
            return;
        }

        // <color>
        final CriticalColor color = sender.getArgumentOptional(0)
                .map(plugin::getColor)
                .orElse(null);
        plugin.data.setColor(color);

        // Message
        if (color == null) {
            plugin.getMessages().get().command.set.none.newMessage().send(sender);
            return;
        }
        plugin.getMessages().get().command.set.message.newMessage()
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
