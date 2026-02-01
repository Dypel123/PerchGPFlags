package me.ryanhamshire.GPFlags.flags;

import me.ryanhamshire.GPFlags.*;
import me.ryanhamshire.GPFlags.util.MessagingUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

public class FlagDef_RescueOnLogin extends FlagDefinition {

    public FlagDef_RescueOnLogin(FlagManager manager, GPFlags plugin) {
        super(manager, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();

        Flag flag = this.getFlagInstanceAtLocation(playerLoc, player);
        if (flag == null) return;

        if (!isPlayerStuck(player)) return;

        // Teleport player to spawn on next tick (after they fully join)
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            Location spawn = player.getWorld().getSpawnLocation();
            player.teleport(spawn);
            MessagingUtil.sendMessage(player, TextMode.Info, Messages.RescuedFromBlock);
        });
    }

    private boolean isPlayerStuck(Player player) {
        Location loc = player.getLocation();

        Material blockAtHead = loc.clone().add(0, 1, 0).getBlock().getType();

        return blockAtHead.isSolid();
    }

    @Override
    public String getName() {
        return "RescueOnLogin";
    }

    @Override
    public MessageSpecifier getSetMessage(String parameters) {
        return new MessageSpecifier(Messages.EnableRescueOnLogin);
    }

    @Override
    public MessageSpecifier getUnSetMessage() {
        return new MessageSpecifier(Messages.DisableRescueOnLogin);
    }

    @Override
    public List<FlagType> getFlagType() {
        return Arrays.asList(FlagType.CLAIM, FlagType.WORLD, FlagType.SERVER);
    }
}