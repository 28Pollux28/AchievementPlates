package eu.pollux28.achievementplates.config;
//Code used from Simplex Terrain <https://github.com/SuperCoder7979/simplexterrain>, with permission from SuperCoder79

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.pollux28.achievementplates.AchievementPlates;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigUtil {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static <T> Constructor<T> getConstructor(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T create(Constructor<T> constructor) {
        if (constructor == null) {
            return null;
        }

        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getFromConfig(Class<T> configClass, Path path) {
        T config = create(getConstructor(configClass));
        try {
            //config exists: return value
            if (Files.exists(path)) {
                config = ConfigUtil.gson.fromJson(new FileReader(path.toFile()), configClass);

                //update to newest config using le epic reflection hacks
                String version = (String) config.getClass().getField("configVersion").get(config);
                if (!version.equals(AchievementPlates.VERSION)) {
                    config.getClass().getField("configVersion").set(config, AchievementPlates.VERSION);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
                    writer.write(ConfigUtil.gson.toJson(config));
                    writer.close();
                }

            } else {
                //config does not exist: write value
                BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
                writer.write(ConfigUtil.gson.toJson(config));
                writer.close();
            }
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return config;
    }
    
    
    public static <T> void configToFile(T config){
        Path path = Paths.get("", "config", "achievement_plates.json");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
            writer.write(ConfigUtil.gson.toJson(config));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}