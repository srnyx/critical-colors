package xyz.srnyx.criticalcolors.commands;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.DefaultReplaceType;
import xyz.srnyx.criticalcolors.CriticalColors;

import java.util.Collections;
import java.util.Set;


public class ColorbarCmd extends xyz.srnyx.criticalcolors.commands.generated.ColorbarCmdGen {
    public ColorbarCmd(@NotNull CriticalColors plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        boolean toggle = !plugin.data.getBossbar();
        if (sender.args.length != 0) toggle = sender.argEquals(0, "on");
        plugin.data.setBossbar(toggle);
        plugin.getMessages().get().command.bar.success.newMessage()
                .replace("%state%", toggle, DefaultReplaceType.BOOLEAN)
                .send(sender);
    }

    @Override @NotNull
    public Set<String> onTabComplete(@NotNull AnnoyingSender sender) {
        return Collections.singleton(plugin.data.getBossbar() ? "off" : "on");
    }
}
