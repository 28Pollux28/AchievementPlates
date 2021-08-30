package eu.pollux28.achievementplates.mixins;

import eu.pollux28.achievementplates.events.AdvancementEarnCallBack;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {
    @Shadow
    private ServerPlayerEntity owner;

    @Inject(method = "Lnet/minecraft/advancement/PlayerAdvancementTracker;grantCriterion(Lnet/minecraft/advancement/Advancement;Ljava/lang/String;)Z",
            at = @At(value = "INVOKE",
                target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V"
            ),
            cancellable = true
    )
    private void injectAdvancementEvent(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> info){
        ActionResult result = AdvancementEarnCallBack.EVENT.invoker().onAdvancement(advancement,this.owner);
        if(result == ActionResult.FAIL) {
            info.cancel();
        }
    }
}
