package eu.pollux28.achievementplates.events;

import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;

import static eu.pollux28.achievementplates.utils.Utils.getPlateItemStack;

public class EarnAdvancementEvent {
    public static AdvancementEarnCallBack onAdvancementEvent(){
        return ((advancement, playerEntity) -> {
            AdvancementDisplay display = advancement.getDisplay();
            if(display !=null && display.shouldShowToast()){
                ItemStack itemStack = getPlateItemStack(playerEntity.getName(), display);
                if (playerEntity.giveItemStack(itemStack)) {
                    playerEntity.world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
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

}
