package eu.pollux28.achievementplates.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface AdvancementEarnCallBack {
    Event<AdvancementEarnCallBack> EVENT = EventFactory.createArrayBacked(AdvancementEarnCallBack.class,
            (listeners)->(advancement, playerEntity) -> {
                for(AdvancementEarnCallBack listener : listeners){
                    ActionResult result = listener.onAdvancement(advancement,playerEntity);

                    if(result != ActionResult.PASS){
                        return result;
                    }
                }
                return ActionResult.PASS;
            });




    ActionResult onAdvancement(Advancement advancement, ServerPlayerEntity playerEntity);

}
