package xyz.srnyx.criticalcolors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.AnnoyingMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerListener implements AnnoyingListener {
    @NotNull private final CriticalColors plugin;
    @NotNull private final Map<UUID, Material> players = new HashMap<>();

    @Contract(pure = true)
    public PlayerListener(@NotNull final CriticalColors plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public CriticalColors getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        // Don't trigger if the player is only looking around
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) return;
        final Player player = event.getPlayer();
        final Material material = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();

        // Detect if player is standing on colored block
        if (plugin.materials.contains(material)) {
            // Kill player
            if (plugin.damage == null || player.getHealth() - plugin.damage <= 0) {
                players.put(player.getUniqueId(), material);
                player.setHealth(0);
                return;
            }

            // Damage player
            player.damage(plugin.damage);
        }
    }

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Material material = players.get(player.getUniqueId());
        if (material == null) return;
        players.remove(player.getUniqueId());
        event.setDeathMessage(new AnnoyingMessage(plugin, "death")
                .replace("%player%", player.getName())
                .replace("%block%", material.name())
                .replace("%color%", plugin.color)
                .toString());
    }
}
