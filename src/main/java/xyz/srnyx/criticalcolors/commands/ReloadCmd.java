package xyz.srnyx.criticalcolors.commands;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;

import xyz.srnyx.criticalcolors.CriticalColors;


public class ReloadCmd extends AnnoyingCommand {
    @NotNull private final CriticalColors plugin;

    public ReloadCmd(@NotNull final CriticalColors plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public CriticalColors getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getName() {
        return "colorreload";
    }

    @Override @NotNull
    public String getPermission() {
        return "criticalcolors.reload";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        plugin.reloadPlugin();
        new AnnoyingMessage(plugin, "command.reload").send(sender);
    }
}
