package eu.pollux28.achievementplates;

import eu.pollux28.achievementplates.block.entity.renderer.PlateBlockEntityRenderer;
import eu.pollux28.achievementplates.config.Config;
import eu.pollux28.achievementplates.config.ConfigUtil;
import eu.pollux28.achievementplates.config.MainConfigData;
import eu.pollux28.achievementplates.events.AdvancementEarnCallBack;
import eu.pollux28.achievementplates.events.EarnAdvancementEvent;
import eu.pollux28.achievementplates.init.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;


public class AchievementPlates implements ModInitializer {

    public static String VERSION = "1.4-0";
    public static final String MODID = "achievement_plates";
    public static final Logger logger = LogManager.getLogger();
    public static MainConfigData CONFIG;
    @Override
    public void onInitialize() {
        ModBlocks.registerBlocks();
        if(FabricLoader.getInstance().getEnvironmentType()== EnvType.CLIENT){
            BlockEntityRendererRegistry.INSTANCE.register(ModBlocks.PLATE_BLOCK_ENTITY, PlateBlockEntityRenderer::new);
        }
        AdvancementEarnCallBack.EVENT.register(EarnAdvancementEvent.onAdvancementEvent());
        CONFIG = Config.init();
        refreshConfig();
        CONFIG.blacklist.get("type").forEach(logger::warn);
        ServerWorldEvents.LOAD.register((server, world) -> refreshConfig());
    }



    public static void refreshConfig(){
        AchievementPlates.CONFIG= ConfigUtil.getFromConfig(MainConfigData.class, Paths.get("", "config", "achievement_plates.json"));
    }
    public static void saveConfig(){
        ConfigUtil.configToFile(CONFIG);
    }
}
