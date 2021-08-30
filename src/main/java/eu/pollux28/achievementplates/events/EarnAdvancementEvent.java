package eu.pollux28.achievementplates.events;

import eu.pollux28.achievementplates.block.PlateBlock;
import eu.pollux28.achievementplates.init.ModBlocks;
import eu.pollux28.achievementplates.item.PlateItem;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

public class EarnAdvancementEvent {
    public static AdvancementEarnCallBack onAdvancementEvent(){
        return ((advancement, playerEntity) -> {
            AdvancementDisplay display = advancement.getDisplay();
            if(display !=null){
                NbtCompound nbt1 = new NbtCompound();
                NbtCompound nbt2 = new NbtCompound();
                writeDisplayToNbt(nbt2,display);
                nbt2.putString("player_name", playerEntity.getName().asString());
                nbt1.put("BlockEntityTag", nbt2);
                ItemStack itemStack = ModBlocks.PLATE_ITEM.getDefaultStack();
                itemStack.setNbt(nbt1);
                itemStack.setCustomName(playerEntity.getName().shallowCopy().setStyle(Style.EMPTY.withColor(Formatting.AQUA)).append(new LiteralText("'s ").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(display.getTitle().shallowCopy().setStyle(Style.EMPTY.withColor(display.getFrame().getTitleFormat()))).append(new LiteralText(" trophy plate").setStyle(Style.EMPTY.withColor(Formatting.GRAY))));
                if (playerEntity.giveItemStack(itemStack)) {
                    playerEntity.world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                } else {
                    ItemEntity itemEntity = playerEntity.dropItem(itemStack, false);
                    if (itemEntity != null) {
                        itemEntity.resetPickupDelay();
                        itemEntity.setOwner(playerEntity.getUuid());
                    }
                }
            }
            return ActionResult.PASS;
        });
    }

    public static void writeDisplayToNbt(NbtCompound nbt, AdvancementDisplay display){
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("display_title",((TranslatableText)display.getTitle()).getKey());
        nbtCompound.putString("display_desc",((TranslatableText)display.getDescription()).getKey());
        if(display.getBackground()!=null){
            nbtCompound.putString("display_background",display.getBackground().toString());
        }
        nbtCompound.put("display_icon",display.getIcon().writeNbt(new NbtCompound()));
        nbtCompound.putString("display_frame_id", display.getFrame().getId());
        nbtCompound.putBoolean("display_toast", display.shouldShowToast());
        nbtCompound.putBoolean("display_chat", display.shouldAnnounceToChat());
        nbtCompound.putBoolean("display_hidden", display.isHidden());
        nbt.put("display", nbtCompound);
    }
}
