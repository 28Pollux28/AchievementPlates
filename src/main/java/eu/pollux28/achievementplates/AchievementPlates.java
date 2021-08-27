package eu.pollux28.achievementplates;

import eu.pollux28.achievementplates.init.ModBlocks;
import net.fabricmc.api.ModInitializer;


public class AchievementPlates implements ModInitializer {

    public static final String MODID = "achievement_plates";
    @Override
    public void onInitialize() {
        ModBlocks.registerBlocks();
    }
}
