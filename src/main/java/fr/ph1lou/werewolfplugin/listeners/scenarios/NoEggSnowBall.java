package fr.ph1lou.werewolfplugin.listeners.scenarios;

import fr.ph1lou.werewolfapi.GetWereWolfAPI;
import fr.ph1lou.werewolfapi.listeners.ListenerManager;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class NoEggSnowBall extends ListenerManager {


    public NoEggSnowBall(GetWereWolfAPI main) {
        super(main);
    }

    @EventHandler
    public void onProjectileThrownEvent(ProjectileLaunchEvent event) {

        if (event.getEntity() instanceof Snowball || event.getEntity() instanceof Egg) {
            event.setCancelled(true);
        }
    }

}
