package eu.pollux28.achievementplates;

import eu.pollux28.achievementplates.block.entity.renderer.PlateBlockEntityRenderer;
import eu.pollux28.achievementplates.events.AdvancementEarnCallBack;
import eu.pollux28.achievementplates.events.EarnAdvancementEvent;
import eu.pollux28.achievementplates.init.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AchievementPlates implements ModInitializer {

    public static final String MODID = "achievement_plates";
    public static final Logger logger = LogManager.getLogger();
    @Override
    public void onInitialize() {
        ModBlocks.registerBlocks();
        if(FabricLoader.getInstance().getEnvironmentType()== EnvType.CLIENT){
            BlockEntityRendererRegistry.INSTANCE.register(ModBlocks.PLATE_BLOCK_ENTITY, PlateBlockEntityRenderer::new);
        }
        AdvancementEarnCallBack.EVENT.register(EarnAdvancementEvent.onAdvancementEvent());
    }
}
