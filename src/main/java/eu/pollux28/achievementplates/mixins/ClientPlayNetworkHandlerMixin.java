package eu.pollux28.achievementplates.mixins;

import eu.pollux28.achievementplates.block.entity.PlateBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onBlockEntityUpdate",
            at = @At(value = "INVOKE_ASSIGN",
                target = "Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;getBlockEntityType()I",
                shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = false)
    private void mixinOnBlockEntityUpdate(BlockEntityUpdateS2CPacket packet,CallbackInfo ci, BlockPos blockPos, BlockEntity blockEntity, int i){
        if(i == PlateBlockEntity.PLATE_BLOCK_ENTITY_ID && blockEntity instanceof PlateBlockEntity entity){
            entity.readNbt(packet.getNbt());
        }
    }
}
