package fr.ph1lou.werewolfplugin.commands.roles.villager.trapper;

import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.commands.ICommand;
import fr.ph1lou.werewolfapi.player.interfaces.IPlayerWW;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.StatePlayer;
import fr.ph1lou.werewolfapi.events.roles.trapper.TrackEvent;
import fr.ph1lou.werewolfapi.role.interfaces.IAffectedPlayers;
import fr.ph1lou.werewolfapi.role.interfaces.IPower;
import fr.ph1lou.werewolfapi.role.interfaces.IRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandTrapper implements ICommand {

    @Override
    public void execute(WereWolfAPI game, Player player, String[] args) {

        UUID uuid = player.getUniqueId();
        IPlayerWW playerWW = game.getPlayerWW(uuid).orElse(null);

        if (playerWW == null) return;

        IRole trapper = playerWW.getRole();
        Player playerArg = Bukkit.getPlayer(args[0]);

        if (playerArg == null) {
            player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.check.offline_player"));
            return;
        }

        UUID argUUID = playerArg.getUniqueId();
        IPlayerWW playerWW1 = game.getPlayerWW(argUUID).orElse(null);

        if (uuid.equals(argUUID)) {
            player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.check.not_yourself"));
            return;
        }

        if (playerWW1 == null || !playerWW1.isState(StatePlayer.ALIVE)) {
            player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.check.player_not_found"));
            return;
        }

        if (((IAffectedPlayers) trapper).getAffectedPlayers().contains(playerWW1)) {
            player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.check.already_get_power"));
            return;
        }

        TrackEvent trackEvent = new TrackEvent(playerWW, playerWW1);
        ((IPower) trapper).setPower(false);
        Bukkit.getPluginManager().callEvent(trackEvent);

        if (trackEvent.isCancelled()) {
            player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.check.cancel"));
            return;
        }

        ((IAffectedPlayers) trapper).clearAffectedPlayer();
        ((IAffectedPlayers) trapper).addAffectedPlayer(playerWW1);

        playerArg.sendMessage(game.translate(Prefix.YELLOW.getKey() , "werewolf.role.trapper.get_track"));
        player.sendMessage(game.translate(Prefix.YELLOW.getKey() , "werewolf.role.trapper.tracking_perform",
                Formatter.player(playerArg.getName())));
    }
}
