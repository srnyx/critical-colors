package xyz.srnyx.criticalcolors.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.data.StringData;
import xyz.srnyx.annoyingapi.file.AnnoyingData;
import xyz.srnyx.annoyingapi.file.AnnoyingFile;

import xyz.srnyx.criticalcolors.CriticalColors;

import java.util.Optional;


public class CriticalData {
    @NotNull public static final String TABLE = "data";
    @NotNull public static final String COL_COLOR = "color";
    @NotNull public static final String COL_ROTATE = "rotate";
    @NotNull public static final String COL_BOSSBAR = "bossbar";

    @NotNull private final CriticalColors plugin;
    @NotNull private final StringData data;

    public CriticalData(@NotNull CriticalColors plugin) {
        this.plugin = plugin;
        this.data = new StringData(plugin, TABLE, "server");
    }

    /**
     * @deprecated  will be removed in the future
     */
    @Deprecated
    public void convertOldData() {
        final AnnoyingData file = new AnnoyingData(plugin, "data.yml", new AnnoyingFile.Options<>().canBeEmpty(false));
        if (file.contains("converted_now-stored-elsewhere")) return;

        // color
        final String color = file.getString("color");
        if (color != null) setColor(plugin.getColor(color));
        // rotate
        final Boolean rotate = file.contains("rotate") ? file.getBoolean("rotate") : null;
        if (rotate != null) setRotate(rotate);
        // bossbar
        final Boolean bossbar = file.contains("bossbar") ? file.getBoolean("bossbar") : null;
        if (bossbar != null) setBossbar(bossbar);

        file.setSave("converted_now-stored-elsewhere", true);
    }

    @NotNull
    public Optional<CriticalColor> getColor() {
        return data.getOptional(COL_COLOR).map(plugin::getColor);
    }

    public boolean getRotate() {
        return data.has(COL_ROTATE);
    }

    public boolean getBossbar() {
        return data.has(COL_BOSSBAR);
    }

    public void setColor(@Nullable CriticalColor color) {
        data.set(COL_COLOR, color);
        plugin.updateBar();
    }

    public void setRotate(boolean rotate) {
        data.set(COL_ROTATE, rotate ? true : null);
        plugin.toggleRotating();
    }

    public void setBossbar(boolean bossbar) {
        data.set(COL_BOSSBAR, bossbar ? true : null);
        plugin.updateBar();
    }
}
