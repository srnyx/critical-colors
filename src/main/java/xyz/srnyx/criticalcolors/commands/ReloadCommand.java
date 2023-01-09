package xyz.srnyx.criticalcolors.commands;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingMessage;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;

import xyz.srnyx.criticalcolors.CriticalColors;


public class ReloadCommand implements AnnoyingCommand {
    @NotNull private final CriticalColors plugin;

    @Contract(pure = true)
    public ReloadCommand(@NotNull final CriticalColors plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public AnnoyingPlugin getPlugin() {
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
