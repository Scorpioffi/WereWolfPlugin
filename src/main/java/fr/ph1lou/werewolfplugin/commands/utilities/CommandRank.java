package fr.ph1lou.werewolfplugin.commands.utilities;

import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.commands.ICommand;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.StateGame;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CommandRank implements ICommand {

    @Override
    public void execute(WereWolfAPI game, Player player, String[] args) {

        UUID uuid = player.getUniqueId();

        List<? extends UUID> queue = game.getModerationManager().getQueue();

        if (!game.isState(StateGame.LOBBY)) {
            player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.check.already_begin"));
            return;
        }

        if (queue.contains(uuid)) {
            player.sendMessage(game.translate(Prefix.GREEN.getKey() , "werewolf.menu.rank.perform",
                    Formatter.number(queue.indexOf(uuid) + 1)));
        } else {
            player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.menu.rank.not_in_queue"));
        }
    }
}
