package xyz.srnyx.criticalcolors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.file.AnnoyingData;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;

import xyz.srnyx.criticalcolors.commands.ColorCommand;
import xyz.srnyx.criticalcolors.commands.ReloadCommand;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class CriticalColors extends AnnoyingPlugin {
    public ConfigurationSection section;
    public AnnoyingData data;
    @Nullable public String color;
    @NotNull public Set<Material> materials = new HashSet<>();
    @Nullable public Double damage;

    public CriticalColors() {
        super();
        reload();

        // Options
        options.colorLight = ChatColor.LIGHT_PURPLE;
        options.colorDark = ChatColor.DARK_PURPLE;
        options.commands.add(new ReloadCommand(this));
        options.commands.add(new ColorCommand(this));
        options.listeners.add(new PlayerListener(this));
    }

    @Override
    public void reload() {
        final AnnoyingResource config = new AnnoyingResource(this, "config.yml");

        section = config.getConfigurationSection("colors");
        data = new AnnoyingData(this, "data.yml", false);
        setColor(data.getString("color"));

        // Set damage
        final double damageConfig = config.getDouble("damage");
        damage = damageConfig == 0 ? null : damageConfig;
    }

    public void setColor(@Nullable String newColor) {
        color = newColor;

        // Update materials
        if (color == null) {
            materials.clear();
            return;
        }
        materials = section.getStringList(color).stream()
                .map(Material::getMaterial)
                .collect(Collectors.toSet());
    }
}
