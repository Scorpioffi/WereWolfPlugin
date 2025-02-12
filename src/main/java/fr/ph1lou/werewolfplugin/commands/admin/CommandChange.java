package fr.ph1lou.werewolfplugin.commands.admin;

import fr.ph1lou.werewolfapi.commands.ICommand;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Prefix;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandChange implements ICommand {

    @Override
    public void execute(WereWolfAPI game, Player player, String[] args) {

        player.sendMessage(game.translate(Prefix.YELLOW.getKey() , "werewolf.commands.admin.change.in_progress"));


        try {
            game.getMapManager().loadMap();
        } catch (IOException ignored) {
        }

        player.sendMessage(game.translate(Prefix.GREEN.getKey() , "werewolf.commands.admin.change.finished"));

    }
}
