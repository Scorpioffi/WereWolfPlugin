package fr.ph1lou.werewolfplugin.roles.villagers;

import fr.ph1lou.werewolfapi.role.utils.DescriptionBuilder;
import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.player.interfaces.IPlayerWW;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.StatePlayer;
import fr.ph1lou.werewolfapi.enums.TimerBase;
import fr.ph1lou.werewolfapi.events.game.day_cycle.DayEvent;
import fr.ph1lou.werewolfapi.role.interfaces.IAffectedPlayers;
import fr.ph1lou.werewolfapi.role.interfaces.ILimitedUse;
import fr.ph1lou.werewolfapi.role.impl.RoleWithLimitedSelectionDuration;
import fr.ph1lou.werewolfapi.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Analyst extends RoleWithLimitedSelectionDuration implements ILimitedUse, IAffectedPlayers {

    private int use = 0;
    private boolean power2 = true;
    private final List<IPlayerWW> affectedPlayers = new ArrayList<>();

    public Analyst(WereWolfAPI game, IPlayerWW playerWW, String key) {
        super(game, playerWW, key);
        this.setPower(false);
    }

    @Override
    public @NotNull String getDescription() {
        return new DescriptionBuilder(game,this)
                .setDescription(game.translate("werewolf.role.analyst.description",
                        Formatter.timer(
                                Utils.conversion(
                                        Math.max(0,
                                                Math.max(0,
                                                        game.getConfig().getTimerValue(TimerBase.ROLE_DURATION.getKey()))+
                                                        game.getConfig().getTimerValue(TimerBase.ANALYSE_DURATION.getKey()))))))
                .build();
    }

    @Override
    public void recoverPower() {

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDay(DayEvent event) {

        if(this.use >= 5){
            return;
        }

        if(game.getConfig().getTimerValue(TimerBase.ANALYSE_DURATION.getKey()) > 0){
            return;
        }

        setPower(true);

        if (!this.getPlayerWW().isState(StatePlayer.ALIVE)) {
            return;
        }

        this.getPlayerWW().sendMessageWithKey(
                Prefix.YELLOW.getKey() , "werewolf.role.analyst.message_see",
                Formatter.number(5 - this.use),
                Formatter.timer(Utils.conversion(
                        game.getConfig().getTimerValue(TimerBase.POWER_DURATION.getKey()))));
    }

    @Override
    public int getUse() {
        return this.use;
    }

    @Override
    public void setUse(int use) {
        this.use = use;
    }

    @Override
    public void addAffectedPlayer(IPlayerWW iPlayerWW) {
        this.affectedPlayers.add(iPlayerWW);
    }

    @Override
    public void removeAffectedPlayer(IPlayerWW iPlayerWW) {
        this.affectedPlayers.remove(iPlayerWW);
    }

    @Override
    public void clearAffectedPlayer() {
        this.affectedPlayers.clear();
    }

    @Override
    public List<? extends IPlayerWW> getAffectedPlayers() {
        return this.affectedPlayers;
    }

    public boolean isPower2() {
        return power2;
    }

    public void setPower2(boolean power2) {
        this.power2 = power2;
    }
}
