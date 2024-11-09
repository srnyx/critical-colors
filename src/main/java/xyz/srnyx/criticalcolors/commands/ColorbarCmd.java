package xyz.srnyx.criticalcolors.commands;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.message.DefaultReplaceType;

import xyz.srnyx.criticalcolors.CriticalColors;

import java.util.Collections;
import java.util.Set;


public class ColorbarCmd extends AnnoyingCommand {
    @NotNull private final CriticalColors plugin;

    public ColorbarCmd(@NotNull CriticalColors plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public CriticalColors getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "criticalcolors.colorbar";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        boolean toggle = !plugin.data.getBossbar();
        if (sender.args.length != 0) toggle = sender.argEquals(0, "on");
        plugin.data.setBossbar(toggle);
        new AnnoyingMessage(plugin, "command.bar.success")
                .replace("%state%", toggle, DefaultReplaceType.BOOLEAN)
                .send(sender);
    }

    @Override @NotNull
    public Set<String> onTabComplete(@NotNull AnnoyingSender sender) {
        return Collections.singleton(plugin.data.getBossbar() ? "off" : "on");
    }
}
