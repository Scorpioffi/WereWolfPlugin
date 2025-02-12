package fr.ph1lou.werewolfplugin.roles.werewolfs;


import fr.ph1lou.werewolfapi.role.utils.DescriptionBuilder;
import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.player.interfaces.IPlayerWW;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.StatePlayer;
import fr.ph1lou.werewolfapi.events.game.life_cycle.SecondDeathEvent;
import fr.ph1lou.werewolfapi.role.interfaces.IAffectedPlayers;
import fr.ph1lou.werewolfapi.role.interfaces.IPower;
import fr.ph1lou.werewolfapi.role.impl.RoleWereWolf;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfectFatherOfTheWolves extends RoleWereWolf implements IAffectedPlayers, IPower {

    private final List<IPlayerWW> affectedPlayer = new ArrayList<>();
    private boolean power = true;

    public InfectFatherOfTheWolves(WereWolfAPI api,
                                   IPlayerWW playerWW,
                                   String key) {
        super(api, playerWW, key);
    }

    @Override
    public void setPower(boolean power) {
        this.power = power;
    }

    @Override
    public boolean hasPower() {
        return (this.power);
    }

    @Override
    public void addAffectedPlayer(IPlayerWW playerWW) {
        this.affectedPlayer.add(playerWW);
    }

    @Override
    public void removeAffectedPlayer(IPlayerWW playerWW) {
        this.affectedPlayer.remove(playerWW);
    }

    @Override
    public void clearAffectedPlayer() {
        this.affectedPlayer.clear();
    }

    @Override
    public List<IPlayerWW> getAffectedPlayers() {
        return (this.affectedPlayer);
    }


    @Override
    public @NotNull String getDescription() {

        return new DescriptionBuilder(game, this)
                .setDescription(game.translate("werewolf.role.infect_father_of_the_wolves.description"))
                .setEffects(game.translate("werewolf.description.werewolf"))
                .setPower(game.translate(power ? "werewolf.role.infect_father_of_the_wolves.power_available" : "werewolf.role.infect_father_of_the_wolves.power_not_available"))
                .setItems(game.translate("werewolf.role.infect_father_of_the_wolves.items"))
                .build();
    }


    @Override
    public void recoverPower() {

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSecondDeathEvent(SecondDeathEvent event) {

        if (event.isCancelled()) return;

        if (!hasPower()) return;

        if (!isAbilityEnabled()) return;

        IPlayerWW playerWW = event.getPlayerWW();


        Optional<IPlayerWW> killerWW = playerWW.getLastKiller();


        if (!killerWW.isPresent()) {
            return;
        }

        if (!killerWW.get().getRole().isWereWolf()) {
            return;
        }

        if (playerWW.equals(getPlayerWW())) {
            return;
        }

        if (!getPlayerWW().isState(StatePlayer.ALIVE)) return;

        TextComponent infectMessage = new TextComponent(
                game.translate(
                        Prefix.YELLOW.getKey() , "werewolf.role.infect_father_of_the_wolves.infection_message",
                        Formatter.player(playerWW.getName())));
        infectMessage.setClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                        String.format("/ww %s %s",
                                game.translate("werewolf.role.infect_father_of_the_wolves.command"),
                                playerWW.getUUID())));
        getPlayerWW().sendMessage(infectMessage);
    }
}
