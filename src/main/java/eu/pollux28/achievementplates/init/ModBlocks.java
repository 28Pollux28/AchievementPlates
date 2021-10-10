package eu.pollux28.achievementplates.init;

import eu.pollux28.achievementplates.AchievementPlates;
import eu.pollux28.achievementplates.block.PlateBlock;
import eu.pollux28.achievementplates.block.WallPlateBlock;
import eu.pollux28.achievementplates.block.entity.PlateBlockEntity;
import eu.pollux28.achievementplates.item.PlateItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModBlocks {

    public static final Block PLATE_BLOCK = new PlateBlock(FabricBlockSettings.of(Material.DECORATION).strength(1f,0.5f));
    public static final Block WALL_PLATE_BLOCK = new WallPlateBlock(FabricBlockSettings.of(Material.DECORATION).strength(1f,0.5f));
    public static BlockEntityType<PlateBlockEntity>PLATE_BLOCK_ENTITY;
    public static Item PLATE_ITEM;
    public static ItemStack PLATE_ITEM_STACK;


    public static void registerBlocks(){
        registerAsBlock("achievement_plate",PLATE_BLOCK);
        registerAsBlock("achievement_wall_plate",WALL_PLATE_BLOCK);
        registerItems();
        PLATE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(AchievementPlates.MODID,"achievement_plate"), FabricBlockEntityTypeBuilder.create(PlateBlockEntity::new, PLATE_BLOCK, WALL_PLATE_BLOCK).build(null));
    }


    private static void registerAsBlock(String name, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(AchievementPlates.MODID, name), block);
    }


    private static void registerItems(){
        PLATE_ITEM =Registry.register(Registry.ITEM, new Identifier(AchievementPlates.MODID,"achievement_plate"),new PlateItem(PLATE_BLOCK,WALL_PLATE_BLOCK,new FabricItemSettings()/*.group(ItemGroup.DECORATIONS)*/));
        PLATE_ITEM_STACK = PLATE_ITEM.getDefaultStack();
//        NbtCompound nbtCompound = new NbtCompound();
//        nbtCompound.putBoolean("give",true);
//        PLATE_ITEM_STACK.setTag(nbtCompound);
    }

}
