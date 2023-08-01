package xyz.srnyx.criticalcolors.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.events.AnnoyingPlayerMoveEvent;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;

import xyz.srnyx.criticalcolors.CriticalColors;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static xyz.srnyx.criticalcolors.reflection.org.bukkit.boss.RefBossBar.*;


public class PlayerListener implements AnnoyingListener {
    @NotNull private final CriticalColors plugin;
    @NotNull private final Map<UUID, Material> players = new HashMap<>();

    public PlayerListener(@NotNull CriticalColors plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public CriticalColors getAnnoyingPlugin() {
        return plugin;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        if (plugin.bossBar != null && BOSS_BAR_ADD_PLAYER_METHOD != null) try {
            BOSS_BAR_ADD_PLAYER_METHOD.invoke(plugin.bossBar, event.getPlayer());
        } catch (final InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerMove(@NotNull AnnoyingPlayerMoveEvent event) {
        if (event.getMovementType() != AnnoyingPlayerMoveEvent.MovementType.TRANSLATION || plugin.data.color == null) return;
        final Player player = event.getPlayer();

        // Don't trigger if the player's in creative or spectator
        final GameMode gameMode = player.getGameMode();
        if (gameMode.equals(GameMode.CREATIVE) || gameMode.equals(GameMode.SPECTATOR)) return;

        // Detect if player is standing on colored block
        final Material material = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if (!plugin.data.color.materials.contains(material)) return;

        // Kill player
        if (plugin.config.damage == null || player.getHealth() - plugin.config.damage <= 0) {
            players.put(player.getUniqueId(), material);
            player.setHealth(0);
            return;
        }
        // Damage player
        player.damage(plugin.config.damage);
    }

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Material material = players.get(player.getUniqueId());
        if (material == null) return;
        players.remove(player.getUniqueId());
        if (plugin.data.color != null) event.setDeathMessage(new AnnoyingMessage(plugin, "death")
                .replace("%player%", player.getName())
                .replace("%block%", material.name())
                .replace("%color%", plugin.data.color.color)
                .toString());
    }
}
