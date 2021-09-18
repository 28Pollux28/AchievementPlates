package eu.pollux28.achievementplates.events;

import eu.pollux28.achievementplates.AchievementPlates;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;
import java.util.Map;

import static eu.pollux28.achievementplates.utils.Utils.getPlateItemStack;

public class EarnAdvancementEvent {
    public static AdvancementEarnCallBack onAdvancementEvent(){
        return ((advancement, playerEntity) -> {
            AdvancementDisplay display = advancement.getDisplay();
            if(display !=null && display.shouldShowToast()){
                boolean flag = true;
                if(AchievementPlates.CONFIG.useWhitelist){
                    Map<String,ArrayList<String>> whitelist = AchievementPlates.CONFIG.whitelist;
                    boolean namespace = false;
                    boolean id = false;
                    boolean type = false;
                    if(whitelist.containsKey("namespace") && whitelist.get("namespace").contains(advancement.getId().getNamespace())){
                        namespace = true;
                    }
                    if(whitelist.containsKey("identifier") && whitelist.get("identifier").contains(advancement.getId().toString())){
                        id = true;
                    }
                    if(whitelist.containsKey("type") && whitelist.get("type").contains(display.getFrame().getId())){
                        type = true;
                    }
                    flag = flag &&(namespace||id||type);
                }
                if(AchievementPlates.CONFIG.useBlacklist){
                    Map<String,ArrayList<String>> blacklist = AchievementPlates.CONFIG.blacklist;
                    boolean namespace = true;
                    boolean id = true;
                    boolean type = true;
                    if(blacklist.containsKey("namespace") && blacklist.get("namespace").contains(advancement.getId().getNamespace())){
                        namespace = false;
                    }
                    if(blacklist.containsKey("identifier") && blacklist.get("identifier").contains(advancement.getId().toString())){
                        id = false;
                    }
                    if(blacklist.containsKey("type") && blacklist.get("type").contains(display.getFrame().getId())){
                        type = false;
                    }
                    flag = flag &&(namespace&&id&&type);
                }
                if(!flag) return ActionResult.PASS;

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
