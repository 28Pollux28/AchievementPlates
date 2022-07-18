package eu.pollux28.achievementplates;

import eu.pollux28.achievementplates.advancement.PlayerTrophyManager;
import eu.pollux28.achievementplates.block.entity.renderer.PlateBlockEntityRenderer;
import eu.pollux28.achievementplates.command.Commands;
import eu.pollux28.achievementplates.config.Config;
import eu.pollux28.achievementplates.config.ConfigUtil;
import eu.pollux28.achievementplates.config.MainConfigData;
import eu.pollux28.achievementplates.init.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.HashMap;


public class AchievementPlates implements ModInitializer {

    public static String VERSION = "1.6.1";
    public static final String MODID = "achievement_plates";
    public static final Logger logger = LogManager.getLogger();
    public static MainConfigData CONFIG;
    public static HashMap<ServerPlayerEntity,PlayerTrophyManager> trophyManagers = new HashMap<>();
    @Override
    public void onInitialize() {
        ModBlocks.registerBlocks();
        Commands.init();
        if(FabricLoader.getInstance().getEnvironmentType()== EnvType.CLIENT){
            BlockEntityRendererRegistry.register(ModBlocks.PLATE_BLOCK_ENTITY, PlateBlockEntityRenderer::new);
        }
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if(!AchievementPlates.CONFIG.useClaimMessages || server.getTicks()%AchievementPlates.CONFIG.tickDelayBetweenClaimMessages ==0) {
                for (var entryIterator = trophyManagers.entrySet().iterator(); entryIterator.hasNext(); ) {
                    var entry = entryIterator.next();
                    if (entry.getKey().isDisconnected()) {
                        entryIterator.remove();
                    } else {
                        entry.getValue().tick(server);
                    }
                }
            }
        });
        CONFIG = Config.init();
        refreshConfig();
        ServerWorldEvents.LOAD.register((server, world) -> refreshConfig());
    }



    public static void refreshConfig(){
        AchievementPlates.CONFIG= ConfigUtil.getFromConfig(MainConfigData.class, Paths.get("", "config", "achievement_plates.json"));
    }
    public static void saveConfig(){
        ConfigUtil.configToFile(CONFIG);
    }
}
