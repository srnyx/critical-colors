package xyz.srnyx.criticalcolors.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.file.AnnoyingData;
import xyz.srnyx.annoyingapi.file.AnnoyingFile;

import xyz.srnyx.criticalcolors.CriticalColors;


public class CriticalData {
    @NotNull private final CriticalColors plugin;
    @NotNull private final AnnoyingData data;

    @Nullable public CriticalColor color;
    public boolean rotate;
    public boolean bossbar;

    public CriticalData(@NotNull CriticalColors plugin) {
        this.plugin = plugin;
        data = new AnnoyingData(plugin, "data.yml", new AnnoyingFile.Options<>().canBeEmpty(false));
        color = plugin.colors.stream()
                .filter(criticalColor -> criticalColor.color.equals(data.getString("color")))
                .findFirst()
                .orElse(null);
        rotate = data.getBoolean("rotate");
        bossbar = data.getBoolean("bossbar", true);
    }

    public void setColor(@Nullable CriticalColor color) {
        this.color = color;
        data.setSave("color", color == null ? null : color.color);
        plugin.updateBar();
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
        data.setSave("rotate", rotate);
        plugin.toggleRotating();
    }

    public void setBossbar(boolean bossbar) {
        this.bossbar = bossbar;
        data.setSave("bossbar", bossbar);
        plugin.updateBar();
    }
}
