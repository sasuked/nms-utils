package me.saiintbrisson.nms.api.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.saiintbrisson.nms.api.entities.MojangProperty;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SkinFetcher {

    private final static String USERNANE_BASE, LEGACY_BASE, MINESKIN_BASE;

    static {
        USERNANE_BASE = "https://api.mojang.com/users/profiles/minecraft/";
        LEGACY_BASE = "https://sessionserver.mojang.com/session/minecraft/profile/";
        MINESKIN_BASE = "https://api.mineskin.org/generate/user/";
    }

    private static String readURL(String url) throws Exception {
        try(InputStream inputStream = new URL(url).openStream()) {
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder stringBuilder = new StringBuilder();
                String current;
                while ((current = bufferedReader.readLine()) != null) {
                    stringBuilder.append(current);
                }

                inputStream.close();
                bufferedReader.close();

                return stringBuilder.toString();
            }
        }
    }

    public static String fetchId(String userName) {
        try {
            String s = readURL(USERNANE_BASE + userName);
            int i = s.indexOf("\"id\":\"") + 6;
            return s.substring(i, i + 32);
        } catch (Exception ignore) {}

        return null;
    }

    public static MojangProperty fetchSkin(String id, boolean legacy) {
        try {

            JsonParser parser = new JsonParser();
            JsonObject property;

            if(legacy) {
                String s = readURL(LEGACY_BASE + id + "?unsigned=false");
                JsonObject object = parser.parse(s).getAsJsonObject();

                property = object.getAsJsonArray("properties")
                  .get(0)
                  .getAsJsonObject();
            } else {
                String s = readURL(MINESKIN_BASE + id);
                JsonObject object = parser.parse(s).getAsJsonObject();

                property = object.getAsJsonObject("data").getAsJsonObject("texture");
            }

            return new MojangProperty(
              "textures",
              property.get("value").getAsString(),
              property.get("signature").getAsString()
            );

        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        return null;
    }

    public static MojangProperty fetchSkinFromName(String name, boolean legacy) {
        String s = fetchId(name);
        if(s == null) return null;

        return fetchSkin(s, legacy);
    }

}
