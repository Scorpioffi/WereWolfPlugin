package fr.ph1lou.werewolfplugin.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.ph1lou.werewolfapi.game.IConfiguration;
import fr.ph1lou.werewolfapi.statistics.impl.GameReview;

public class Serializer {


    public static Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    public static String serialize(IConfiguration config) {
        return gson().toJson(config);
    }

    public static String serialize(GameReview game) {
        return gson().toJson(game);
    }

    public static Configuration deserialize(String json) {
        return gson().fromJson(json, Configuration.class);
    }

}
