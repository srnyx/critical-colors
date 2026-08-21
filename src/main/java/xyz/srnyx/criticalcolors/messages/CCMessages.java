package xyz.srnyx.criticalcolors.messages;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Include;
import eu.okaeri.configs.annotation.IncludePosition;
import eu.okaeri.validator.annotation.NotNull;
import xyz.srnyx.annoyingapi.file.okaeri.SubConfig;
import xyz.srnyx.annoyingapi.message.AnnoyingMessages;
import xyz.srnyx.annoyingapi.message.json.message.JsonChatMessage;
import xyz.srnyx.annoyingapi.message.json.message.JsonTitleMessage;
import xyz.srnyx.criticalcolors.CriticalColors;


@Include(value = AnnoyingMessages.class, position = IncludePosition.BEFORE)
public class CCMessages extends AnnoyingMessages {
    public CCMessages(@org.jetbrains.annotations.NotNull CriticalColors plugin) {
        super(plugin);
    }

    @Comment
    @Comment("Placeholders: %player%, %block%, %color%")
    @NotNull public JsonChatMessage death = defaultMessage("%pe%%player%%pe% stepped on %se%%block%%pe%, which is a %se%%color%%pe% block!");

    @Comment
    @NotNull public Rotate rotate = new Rotate(this);

    @Comment
    @Comment("Placeholders: %chatcolor%, %color%")
    @NotNull public JsonChatMessage bossbar = defaultMessage("%chatcolor%Current color: &l%color%");

    @Comment
    @NotNull public Command command = new Command(this);

    public static class Rotate extends SubConfig<CCMessages, CCMessages> {
        public Rotate(@org.jetbrains.annotations.NotNull CCMessages defaultsParent) {
            super(defaultsParent);
        }

        @Comment("Placeholders: %chatcolor%, %color%, %delay==time%")
        @NotNull public JsonTitleMessage delay = getRoot().defaultTitle("%chatcolor%%delay==s%", "%chatcolor%%color%");

        @Comment
        @Comment("Placeholders: %chatcolor%, %color%")
        @NotNull public JsonChatMessage set = getRoot().defaultMessage("%chatcolor%The color has changed to &l%color%%chatcolor%!");
    }

    public static class Command extends SubConfig<CCMessages, CCMessages> {
        public Command(@org.jetbrains.annotations.NotNull CCMessages defaultsParent) {
            super(defaultsParent);
        }

        @NotNull public JsonChatMessage reload = getRoot().defaultMessage("%prefix%Plugin reloaded@@%p%%command%@@%command%");

        @Comment
        @NotNull public Get get = new Get(this);

        @Comment
        @NotNull public Set set = new Set(this);

        @Comment
        @NotNull public Bar bar = new Bar(this);

        @Comment
        @Comment("Placeholders: %state==boolean%")
        @NotNull public JsonChatMessage rotate = getRoot().defaultMessage("%prefix%%p%Rotation toggled %s%%state==on//off%@@%p%%command%@@%command%");

        public static class Get extends SubConfig<CCMessages, Command> {
            public Get(@org.jetbrains.annotations.NotNull Command defaultsParent) {
                super(defaultsParent);
            }

            @NotNull public JsonChatMessage none = getRoot().defaultMessage("%prefix%There is currently no color selected!@@%p%%command%@@%command%");

            @Comment
            @Comment("Placeholders: %chatcolor%, %color%")
            @NotNull public JsonChatMessage message = getRoot().defaultMessage("%prefix%The current color is %chatcolor%%color%@@%p%%command%@@%command%");
        }

        public static class Set extends SubConfig<CCMessages, Command> {
            public Set(@org.jetbrains.annotations.NotNull Command defaultsParent) {
                super(defaultsParent);
            }

            @NotNull public JsonChatMessage none = getRoot().defaultMessage("%prefix%Color unset@@%p%%command%@@%command%");

            @Comment
            @Comment("Placeholders: %chatcolor%, %color%")
            @NotNull public JsonChatMessage message = getRoot().defaultMessage("%prefix%Color set to %chatcolor%%color%@@%p%%command%@@%command%");
        }

        public static class Bar extends SubConfig<CCMessages, Command> {
            public Bar(@org.jetbrains.annotations.NotNull Command defaultsParent) {
                super(defaultsParent);
            }

            @NotNull public JsonChatMessage error = getRoot().defaultMessage("%prefix%%pe%Failed to toggle bossbar!@@%pe%%command%@@%command%");

            @Comment
            @Comment("Placeholders: %state==boolean%")
            @NotNull public JsonChatMessage success = getRoot().defaultMessage("%prefix%%p%Bossbar toggled %s%%state==on//off%@@%p%%command%@@%command%");
        }
    }
}
