package eu.pollux28.achievementplates.mixins;

import com.google.common.collect.Maps;
import eu.pollux28.achievementplates.advancement.PlayerTrophyManager;
import eu.pollux28.achievementplates.utils.Utils;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.Set;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {
    @Shadow
    private ServerPlayerEntity owner;
    @Shadow
    private boolean dirty;
    @Final
    @Shadow
    private Set<Advancement> visibleAdvancements;
    @Final
    @Shadow
    private Map<Advancement, AdvancementProgress> advancementToProgress;


    @Inject(method = "sendUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void injectAdvancementLoader(ServerPlayerEntity player, CallbackInfo ci, Map<Identifier, AdvancementProgress> map, Set<Advancement> set, Set<Identifier> set2) {
        Map<Advancement, Boolean> shouldGive = Maps.newHashMap();
        Utils.shouldGiveAdvancement(advancementToProgress, player, shouldGive);
        PlayerTrophyManager playerTrophyManager =PlayerTrophyManager.create(player);
        shouldGive.keySet().stream().filter(shouldGive::get).forEach(playerTrophyManager::addTrophy);
        Utils.removeAdvancement(player, set2);
    }
}
