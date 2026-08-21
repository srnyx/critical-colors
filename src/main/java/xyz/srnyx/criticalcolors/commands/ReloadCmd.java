package xyz.srnyx.criticalcolors.commands;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.criticalcolors.CriticalColors;


public class ReloadCmd extends xyz.srnyx.criticalcolors.commands.generated.ColorreloadCmdGen {
    public ReloadCmd(@NotNull final CriticalColors plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        plugin.reloadPlugin();
        plugin.getMessages().get().command.reload.newMessage().send(sender);
    }
}
