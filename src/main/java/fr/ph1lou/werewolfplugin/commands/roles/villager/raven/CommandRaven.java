package fr.ph1lou.werewolfplugin.commands.roles.villager.raven;

import fr.ph1lou.werewolfapi.player.impl.AuraModifier;
import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.commands.ICommand;
import fr.ph1lou.werewolfapi.player.interfaces.IPlayerWW;
import fr.ph1lou.werewolfapi.player.impl.PotionModifier;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Aura;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.StatePlayer;
import fr.ph1lou.werewolfapi.events.roles.raven.CurseEvent;
import fr.ph1lou.werewolfapi.role.interfaces.IAffectedPlayers;
import fr.ph1lou.werewolfapi.role.interfaces.IPower;
import fr.ph1lou.werewolfapi.role.interfaces.IRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class CommandRaven implements ICommand {

    @Override
    public void execute(WereWolfAPI game, Player player, String[] args) {

        UUID uuid = player.getUniqueId();
        IPlayerWW playerWW = game.getPlayerWW(uuid).orElse(null);

        if (playerWW == null) return;

        IRole raven = playerWW.getRole();
        Player playerArg = Bukkit.getPlayer(args[0]);

        if (playerArg == null) {
            playerWW.sendMessageWithKey(Prefix.RED.getKey() , "werewolf.check.offline_player");
            return;
        }
        UUID argUUID = playerArg.getUniqueId();
        IPlayerWW playerWW1 = game.getPlayerWW(argUUID).orElse(null);

        if (playerWW1 == null || !playerWW1.isState(StatePlayer.ALIVE)) {
            playerWW.sendMessageWithKey(Prefix.RED.getKey() , "werewolf.check.player_not_found");
            return;
        }

        if (((IAffectedPlayers) raven).getAffectedPlayers().contains(playerWW1)) {
            playerWW.sendMessageWithKey(Prefix.RED.getKey() , "werewolf.check.already_get_power");
            return;
        }

        CurseEvent curseEvent = new CurseEvent(playerWW, playerWW1);
        ((IPower) raven).setPower(false);
        Bukkit.getPluginManager().callEvent(curseEvent);

        if (curseEvent.isCancelled()) {
            playerWW.sendMessageWithKey(Prefix.RED.getKey() , "werewolf.check.cancel");
            return;
        }

        ((IAffectedPlayers) raven).clearAffectedPlayer();
        ((IAffectedPlayers) raven).addAffectedPlayer(playerWW1);
        playerWW1.addPotionModifier(PotionModifier.add(PotionEffectType.JUMP,"raven"));
        playerWW1.getRole().addAuraModifier(new AuraModifier("cursed", Aura.DARK, 20, true));
        playerWW1.sendMessageWithKey(Prefix.YELLOW.getKey() , "werewolf.role.raven.get_curse");
        playerWW.sendMessageWithKey(Prefix.YELLOW.getKey() , "werewolf.role.raven.curse_perform",
                Formatter.player(playerArg.getName()));
    }
}
