package eu.pollux28.achievementplates.advancement;

import com.google.gson.JsonObject;
import eu.pollux28.achievementplates.AchievementPlates;
import eu.pollux28.achievementplates.utils.Utils;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerTrophyManager{
    ArrayList<Advancement> toGiveTrophies= new ArrayList<>();
    ServerPlayerEntity playerEntity;
    private PlayerTrophyManager(ServerPlayerEntity playerEntity){
        this.playerEntity = playerEntity;
        AchievementPlates.trophyManagers.put(playerEntity,this);
    }

    public static PlayerTrophyManager create(ServerPlayerEntity playerEntity){
        PlayerTrophyManager playerTrophyManager = AchievementPlates.trophyManagers.get(playerEntity);
        if(playerTrophyManager == null){
            PlayerTrophyManager manager = new PlayerTrophyManager(playerEntity);
            AchievementPlates.trophyManagers.put(playerEntity,manager);
            return manager;
        }
        return playerTrophyManager;
    }
    public void tick(MinecraftServer server){
            if(this.toGiveTrophies.size()!=0){
                if(AchievementPlates.CONFIG.useClaimMessages){
                    sendClaimMessage();
                }else{
                    giveTrophies(null);
                }
            }
    }
    public void addTrophy(Advancement advancement){
        toGiveTrophies.add(advancement);
    }
    public void removeTrophy(Identifier id){
        toGiveTrophies.removeIf(advancement1 -> advancement1.getId()==id);
    }
    public boolean containsTrophy(Identifier id){
        return toGiveTrophies.stream().anyMatch(advancement -> advancement.getId()==id);
    }

    private void sendClaimMessage(){
        Text text =getClaimText();
        playerEntity.sendMessage(new TranslatableText("achievement_plates.chat.claim_message",toGiveTrophies.size(), text).styled(style -> style.withColor(Formatting.YELLOW)), false);
        playerEntity.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.RECORDS,1.0f,1.0f);
    }

    public void giveTrophies(ServerCommandSource source) {
        int n = toGiveTrophies.size();
        Iterator<Advancement> iterator = toGiveTrophies.iterator();
        JsonObject obj = new JsonObject();
        while(iterator.hasNext()){
            Advancement advancement = iterator.next();
            AdvancementDisplay display = advancement.getDisplay();
            ItemStack itemStack = Utils.getPlateItemStack(playerEntity.getName(), display);
            boolean flag = Utils.giveItemToPlayer(playerEntity, itemStack);
            if(!flag){
                break;
            }
            iterator.remove();
            obj.addProperty(advancement.getId().toUnderscoreSeparatedString(),true);

        }
        Utils.writeToJson(playerEntity,obj);
        if(source!=null) {
            int size = toGiveTrophies.size();
            Text text = getClaimText();
            Text text2 = new TranslatableText("achievement_plates.chat.claim_message", toGiveTrophies.size(), text).styled(style -> style.withColor(Formatting.YELLOW));
            MutableText textF = new TranslatableText("achievement_plates.chat.claim_message_not_enough_space", text2).styled(style -> style.withColor(Formatting.YELLOW));

            if (size == n && size != 0) {
                source.sendFeedback(textF, false);
            } else if (size != 0) {
                source.sendFeedback(new TranslatableText("achievement_plates.chat.claim_message_given", n - size).styled(style -> style.withColor(Formatting.GREEN)), false);
                source.sendFeedback(textF, false);
            } else if (n == 0) {
                source.sendFeedback(new TranslatableText("achievement_plates.chat.claim_message_no_trophy").styled(style -> style.withColor(Formatting.RED)), false);
            } else {
                source.sendFeedback(new TranslatableText("achievement_plates.chat.claim_message_given", n - size).styled(style -> style.withColor(Formatting.GREEN)), false);
            }
        }
    }

    private Text getClaimText() {
        return Texts.bracketed(new TranslatableText("achievement_plates.chat.claim_message_action")).styled((style) -> {
            Style var10000 = style.withColor(Formatting.GREEN);
            ClickEvent.Action var10003 = ClickEvent.Action.RUN_COMMAND;
            return var10000.withClickEvent(new ClickEvent(var10003, "/achievement_plates claim")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("achievement_plates.chat.claim_message_tooltip")));
        });
    }
}
