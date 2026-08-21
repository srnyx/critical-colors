package xyz.srnyx.criticalcolors.file.config;

import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.srnyx.annoyingapi.stats.Statable;


/**
 * @param   amount  {@code null} = kill
 */
public record Damage(@Nullable Double amount) implements Statable {
    /**
     * Kill
     */
    public Damage() {
        this(null);
    }

    @Override @NotNull
    public JsonPrimitive toStat() {
        return amount != null ? new JsonPrimitive(amount) : new JsonPrimitive("kill");
    }
}
