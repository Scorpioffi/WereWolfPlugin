package io.github.ph1lou.werewolfplugin.listener.scenarioslisteners;

import io.github.ph1lou.pluginlgapi.enumlg.ScenarioLG;
import io.github.ph1lou.pluginlgapi.enumlg.StateLG;
import io.github.ph1lou.pluginlgapi.enumlg.TimerLG;
import io.github.ph1lou.werewolfplugin.Main;
import io.github.ph1lou.werewolfplugin.game.GameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DiamondLimit extends Scenarios {

    final Map<String, Integer> diamondPerPlayer = new HashMap<>();

    public DiamondLimit(Main main, GameManager game, ScenarioLG diamondLimit) {
        super(main, game,diamondLimit);
    }


    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {

        if (game.isState(StateLG.LOBBY)) return;

        String playerName = event.getPlayer().getName();
        Block block = event.getBlock();

        if (!block.getType().equals(Material.DIAMOND_ORE)) return;

        final Location loc = new Location(block.getWorld(), block.getLocation().getBlockX() + 0.5, block.getLocation().getBlockY() + 0.5, block.getLocation().getBlockZ() + 0.5);

        if (game.getConfig().getTimerValues().get(TimerLG.DIGGING) < 0) {
            block.getWorld().spawn(loc, ExperienceOrb.class).setExperience(event.getExpToDrop());
            block.setType(Material.AIR);
            return;
        }

        if (!event.getPlayer().getItemInHand().getType().equals(Material.DIAMOND_PICKAXE) && !event.getPlayer().getItemInHand().getType().equals(Material.IRON_PICKAXE)) {
            return;
        }
        if (diamondPerPlayer.getOrDefault(playerName, 0) >= game.getConfig().getDiamondLimit()) {
            block.getWorld().dropItem(loc, new ItemStack(Material.GOLD_INGOT, 1));
            block.getWorld().spawn(loc, ExperienceOrb.class).setExperience(event.getExpToDrop());
            block.setType(Material.AIR);
        }
        diamondPerPlayer.put(playerName, diamondPerPlayer.getOrDefault(playerName, 0) + 1);
    }
}