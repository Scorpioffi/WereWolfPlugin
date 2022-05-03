package fr.ph1lou.werewolfplugin.commands.admin.ingame;

import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.commands.ICommand;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandInfo implements ICommand {

    @Override
    public void execute(WereWolfAPI game, Player player, String[] args) {

        if (args.length == 0) {
            player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.check.parameters", Formatter.number(1)));
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (String w : args) {
            sb.append(w).append(" ");
        }
        Bukkit.broadcastMessage(game.translate("werewolf.commands.admin.info.send",
                Formatter.format("&message&",ChatColor.translateAlternateColorCodes('&', sb.toString()))));
    }
}