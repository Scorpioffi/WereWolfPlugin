package fr.ph1lou.werewolfplugin.guis;


import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.ph1lou.werewolfplugin.Main;
import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.StateGame;
import fr.ph1lou.werewolfapi.enums.UniversalMaterial;
import fr.ph1lou.werewolfapi.utils.BukkitUtils;
import fr.ph1lou.werewolfapi.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Maps implements InventoryProvider {


    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("maps")
            .manager(JavaPlugin.getPlugin(Main.class).getInvManager())
            .provider(new Maps())
            .size(2, 9)
            .title(JavaPlugin.getPlugin(Main.class).getWereWolfAPI().translate("werewolf.menu.maps.name"))
            .closeable(true)
            .build();


    @Override
    public void init(Player player, InventoryContents contents) {
        Main main = JavaPlugin.getPlugin(Main.class);
        WereWolfAPI game = main.getWereWolfAPI();
        contents.set(0, 0, ClickableItem.of((new ItemBuilder(UniversalMaterial.COMPASS.getType()).setDisplayName(game.translate("werewolf.menu.return")).build()), e -> Config.INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        Main main = JavaPlugin.getPlugin(Main.class);
        WereWolfAPI game = main.getWereWolfAPI();

        File repertoire = new File(main.getDataFolder() + File.separator + "maps");
        File[] files = repertoire.listFiles();
        if (files == null) return;
        int i = 1;

        for (File file : files) {
            contents.set(0, i, ClickableItem.of((
                            new ItemBuilder(UniversalMaterial.MAP.getType())
                                    .setDisplayName(game.translate("werewolf.menu.maps.map",
                                            Formatter.format("&world&",file.getName()))).build()),
                    e -> {
                        if (!game.isState(StateGame.LOBBY)) {
                            player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.check.game_in_progress"));
                            return;
                        }
                        try {
                            game.getMapManager().loadMap(file);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }));
            i++;
        }


        contents.set(1, 1, ClickableItem.of((
                        new ItemBuilder(UniversalMaterial.LAVA_BUCKET.getType())
                                .setDisplayName(game.translate("werewolf.menu.maps.new")).build()),
                e -> BukkitUtils.scheduleSyncDelayedTask(() -> {
                    if (!game.isState(StateGame.LOBBY)) {
                        player.sendMessage(game.translate(Prefix.RED.getKey() , "werewolf.check.game_in_progress"));
                        return;
                    }
                    try {
                        player.closeInventory();
                        game.getMapManager().loadMap(null);
                    } catch (IOException ignored) {
                    }
                })));
    }


}

