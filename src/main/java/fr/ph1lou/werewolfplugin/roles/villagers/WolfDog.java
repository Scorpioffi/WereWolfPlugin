package fr.ph1lou.werewolfplugin.roles.villagers;


import fr.ph1lou.werewolfapi.role.utils.DescriptionBuilder;
import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.player.interfaces.IPlayerWW;
import fr.ph1lou.werewolfapi.player.impl.PotionModifier;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Aura;
import fr.ph1lou.werewolfapi.enums.Camp;
import fr.ph1lou.werewolfapi.enums.ConfigBase;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.StatePlayer;
import fr.ph1lou.werewolfapi.enums.TimerBase;
import fr.ph1lou.werewolfapi.events.game.day_cycle.NightEvent;
import fr.ph1lou.werewolfapi.events.game.timers.WereWolfListEvent;
import fr.ph1lou.werewolfapi.events.werewolf.AppearInWereWolfListEvent;
import fr.ph1lou.werewolfapi.events.werewolf.WereWolfCanSpeakInChatEvent;
import fr.ph1lou.werewolfapi.events.werewolf.WereWolfChatEvent;
import fr.ph1lou.werewolfapi.role.interfaces.IPower;
import fr.ph1lou.werewolfapi.role.interfaces.IRole;
import fr.ph1lou.werewolfapi.role.interfaces.ITransformed;
import fr.ph1lou.werewolfapi.role.impl.RoleVillage;
import fr.ph1lou.werewolfapi.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class WolfDog extends RoleVillage implements ITransformed, IPower {

    private boolean transformed = false;
    private boolean power = true;

    public WolfDog(WereWolfAPI api, IPlayerWW playerWW, String key) {
        super(api, playerWW, key);
    }

    @Override
    public @NotNull String getDescription() {

        return new DescriptionBuilder(game, this)
                .setDescription(power ?
                        game.translate("werewolf.role.wolf_dog.description")
                                + '\n' + game.translate("werewolf.role.wolf_dog.description_2")
                        :
                        game.translate(this.transformed ? "werewolf.role.wolf_dog.description_2"
                                :
                                "werewolf.role.wolf_dog.description"))
                .build();

    }


    @Override
    public void recoverPower() {

        int timer = game.getConfig().getTimerValue(TimerBase.WEREWOLF_LIST.getKey());

        if (timer > 0) {
            this.getPlayerWW().sendMessageWithKey(Prefix.GREEN.getKey() , "werewolf.role.wolf_dog.transform",
                    Formatter.timer(Utils.conversion(game.getConfig().getTimerValue(TimerBase.WEREWOLF_LIST.getKey()))));
        }
    }

    @Override
    public boolean isWereWolf() {
        return super.isWereWolf() || this.transformed;
    }

    @Override
    public Aura getDefaultAura() {
        return this.transformed ? Aura.LIGHT : Aura.DARK;
    }

    @EventHandler
    public void onWereWolfList(WereWolfListEvent event) {

        if (this.power) {
            this.getPlayerWW().sendMessageWithKey(Prefix.RED.getKey() , "werewolf.role.wolf_dog.time_over");
        }
        this.power = false;
    }

    @Override
    @EventHandler
    public void onWWChat(WereWolfChatEvent event) {

        if (event.isCancelled()) return;

        if (this.transformed && !super.isWereWolf()) {
            event.setCancelled(true);
            return;
        }

        if (!this.getPlayerWW().isState(StatePlayer.ALIVE)) return;

        event.sendMessage(this.getPlayerWW());

    }

    @Override
    @EventHandler
    public void onNightForWereWolf(NightEvent event) {

        if (!isAbilityEnabled()) return;

        if (super.isWereWolf()) {
            this.getPlayerWW().addPotionModifier(PotionModifier.add(PotionEffectType.INCREASE_DAMAGE,"werewolf"));
        }

        if (transformed || !super.isWereWolf()) return;


        if (!game.getConfig().isConfigActive(ConfigBase.WEREWOLF_CHAT.getKey())) return;

        openWereWolfChat();

    }

    @Override
    @EventHandler
    public void onChatSpeak(WereWolfCanSpeakInChatEvent event) {

        if (!this.getPlayerWW().isState(StatePlayer.ALIVE)) return;

        if (!event.getPlayerWW().equals(getPlayerWW())) return;

        if (this.transformed && !super.isWereWolf()) return;

        event.setCanSpeak(true);
    }

    @Override
    @EventHandler
    public void onAppearInWereWolfList(AppearInWereWolfListEvent event) {

        if (!getPlayerUUID().equals(event.getPlayerUUID())) return;

        if (this.getPlayerWW().isState(StatePlayer.DEATH)) return;

        event.setAppear(!this.transformed || super.isWereWolf());
    }

    @Override
    public boolean isDisplayCamp(String camp) {
        return getDisplayCamp().equals(camp);
    }

    @Override
    public String getDisplayCamp() {

        if (this.transformed) {
            return Camp.VILLAGER.getKey();
        }
        return Camp.WEREWOLF.getKey();
    }

    @Override
    public String getDisplayRole() {

        if (this.transformed) {
            return this.game.getPlayersWW().stream()
                    .filter(playerWW -> playerWW.isState(StatePlayer.ALIVE))
                    .map(IPlayerWW::getRole)
                    .filter(roles -> roles.isCamp(Camp.VILLAGER))
                    .map(IRole::getKey)
                    .findFirst()
                    .orElse(this.getKey());
        }


        return this.game.getPlayersWW().stream()
                .filter(playerWW -> playerWW.isState(StatePlayer.ALIVE))
                .map(IPlayerWW::getRole)
                .filter(role -> role.isDisplayCamp(Camp.WEREWOLF.getKey()))
                .map(IRole::getKey)
                .findFirst()
                .orElse(this.getKey());
    }

    @Override
    public boolean isTransformed() {
        return this.transformed;
    }

    @Override
    public void setTransformed(boolean transformed) {
        this.transformed = transformed;
    }

    @Override
    public void setPower(boolean power) {
        this.power = power;
    }

    @Override
    public boolean hasPower() {
        return power;
    }
}
