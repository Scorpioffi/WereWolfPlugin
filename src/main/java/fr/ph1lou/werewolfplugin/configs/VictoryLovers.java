package fr.ph1lou.werewolfplugin.configs;

import fr.ph1lou.werewolfapi.GetWereWolfAPI;
import fr.ph1lou.werewolfapi.annotations.Configuration;
import fr.ph1lou.werewolfapi.basekeys.ConfigBase;
import fr.ph1lou.werewolfapi.enums.LoverType;
import fr.ph1lou.werewolfapi.events.game.utils.WinConditionsCheckEvent;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.listeners.impl.ListenerManager;
import fr.ph1lou.werewolfapi.lovers.ILover;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@Configuration(key = ConfigBase.VICTORY_LOVERS)
public class VictoryLovers extends ListenerManager {

    public VictoryLovers(GetWereWolfAPI main) {
        super(main);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDetectVictoryCancel(WinConditionsCheckEvent event) {

        if (event.isCancelled()) return;

        WereWolfAPI game = this.getGame();

        if (game.getLoversManager().getLovers().stream()
                .filter(ILover::isAlive).anyMatch(ILover -> ILover.isKey(LoverType.LOVER.getKey()))) {
            event.setCancelled(true);
            return;
        }

        if (game.getLoversManager().getLovers().stream()
                .filter(ILover::isAlive).anyMatch(ILover -> ILover.isKey(LoverType.AMNESIAC_LOVER.getKey()))) {
            event.setCancelled(true);
        }
    }


}