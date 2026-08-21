package xyz.srnyx.criticalcolors.messages;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.file.okaeri.ConfigBuilder;
import xyz.srnyx.annoyingapi.message.MessagesProvider;
import xyz.srnyx.criticalcolors.CriticalColors;


public class CCMessagesProvider extends MessagesProvider {
    @NotNull private final CriticalColors plugin;

    public CCMessagesProvider(@NotNull CriticalColors plugin) {
        this.plugin = plugin;

        defaults
                .prefix("&5&lCOLORS &8&l| &d")
                .p("&d")
                .s("&5");
    }

    @Override @NotNull
    public CriticalColors getAnnoyingPlugin() {
        return plugin;
    }

    @Override
    public void mutateBuilder(@NotNull ConfigBuilder builder) {
        builder.config(new CCMessages(plugin));
    }

    @Override @NotNull
    public CCMessages get() {
        return (CCMessages) messages;
    }
}
