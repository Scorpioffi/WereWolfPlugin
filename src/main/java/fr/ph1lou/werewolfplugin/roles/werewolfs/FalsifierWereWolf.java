package fr.ph1lou.werewolfplugin.roles.werewolfs;

import fr.ph1lou.werewolfapi.role.utils.DescriptionBuilder;
import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.player.interfaces.IPlayerWW;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Aura;
import fr.ph1lou.werewolfapi.enums.Camp;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.RolesBase;
import fr.ph1lou.werewolfapi.enums.StatePlayer;
import fr.ph1lou.werewolfapi.events.roles.SelectionEndEvent;
import fr.ph1lou.werewolfapi.events.roles.falsifier_werewolf.NewDisplayRole;
import fr.ph1lou.werewolfapi.role.interfaces.IRole;
import fr.ph1lou.werewolfapi.role.impl.RoleWereWolf;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FalsifierWereWolf extends RoleWereWolf {

    private Aura displayAura;

    public FalsifierWereWolf(WereWolfAPI api, IPlayerWW playerWW, String key) {
        super(api, playerWW, key);
        this.setDisplayCamp(Camp.VILLAGER.getKey());
        this.setDisplayRole(RolesBase.VILLAGER.getKey());
        this.displayAura = Aura.LIGHT;
    }

    @EventHandler
    public void onSelectionEnd(SelectionEndEvent event) {


        if (!this.getPlayerWW().isState(StatePlayer.ALIVE)) {
            return;
        }

        if (!isAbilityEnabled()) {
            this.setDisplayCamp(Camp.WEREWOLF.getKey());
            this.setDisplayRole(this.getKey());
            this.displayAura = Aura.DARK;
            return;
        }

        List<UUID> players = new ArrayList<>();
        for (IPlayerWW playerWW1 : game.getPlayersWW()) {
            if (playerWW1.isState(StatePlayer.ALIVE) && !playerWW1.equals(getPlayerWW())) {
                players.add(playerWW1.getUUID());
            }
        }
        if (players.size() <= 0) {
            return;
        }

        IPlayerWW displayWW = game.autoSelect(getPlayerWW());

        IRole roles = displayWW.getRole();
        NewDisplayRole newDisplayRole = new NewDisplayRole(this.getPlayerWW(), roles.getKey(), roles.getCamp().getKey());
        Bukkit.getPluginManager().callEvent(newDisplayRole);

        if (newDisplayRole.isCancelled()) {
            this.getPlayerWW().sendMessageWithKey(Prefix.RED.getKey() , "werewolf.check.cancel");
            setDisplayCamp(Camp.WEREWOLF.getKey());
            setDisplayRole(RolesBase.FALSIFIER_WEREWOLF.getKey());
            displayAura = Aura.DARK;
        } else {
            setDisplayRole(roles.getKey());
            setDisplayCamp(newDisplayRole.getNewDisplayCamp());
            displayAura = roles.getDefaultAura();
        }
        this.getPlayerWW().sendMessageWithKey(Prefix.YELLOW.getKey() , "werewolf.role.falsifier_werewolf.display_role_message",
                Formatter.role(game.translate(getDisplayRole())));
    }


    @Override
    public @NotNull String getDescription() {

        return new DescriptionBuilder(game, this)
                .setDescription(game.translate("werewolf.role.falsifier_werewolf.description"))
                .setEffects(game.translate("werewolf.description.werewolf"))
                .addExtraLines(game.translate("werewolf.role.falsifier_werewolf.role",
                                Formatter.role(game.translate(this.getDisplayRole()))))
                .build();
    }


    @Override
    public void recoverPower() {

    }

    @Override
    public Aura getDefaultAura() {
        return this.displayAura;
    }

    @Override
    public Aura getAura() {
        return this.displayAura;
    }
}
