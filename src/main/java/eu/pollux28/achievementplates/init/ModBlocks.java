package eu.pollux28.achievementplates.init;

import eu.pollux28.achievementplates.AchievementPlates;
import eu.pollux28.achievementplates.block.PlateBlock;
import eu.pollux28.achievementplates.block.entity.PlateBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.swing.text.html.parser.Entity;


public class ModBlocks {

    public static final Block PLATE_BLOCK = new PlateBlock(FabricBlockSettings.of(Material.DECORATION).strength(1.0f));
    public static BlockEntityType<PlateBlockEntity>PLATE_BLOCK_ENTITY;
    public static Item PLATE_ITEM;
    public static ItemStack PLATE_ITEM_STACK;


    public static void registerBlocks(){
        registerBlockItem("achievement_plate",PLATE_BLOCK);
        PLATE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(AchievementPlates.MODID,"achievement_plate"), FabricBlockEntityTypeBuilder.create(PlateBlockEntity::new, PLATE_BLOCK).build(null));
    }


    private static void registerAsBlock(String name, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(AchievementPlates.MODID, name), block);
    }


    private static void registerBlockItem(String name, Block block){
        registerAsBlock(name, block);
        PLATE_ITEM =Registry.register(Registry.ITEM, new Identifier(AchievementPlates.MODID,name),new BlockItem(PLATE_BLOCK,new FabricItemSettings()/*.group(ItemGroup.DECORATIONS)*/));
        PLATE_ITEM_STACK = PLATE_ITEM.getDefaultStack();
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putBoolean("give",true);
        PLATE_ITEM_STACK.setNbt(nbtCompound);
    }

}
