package eu.pollux28.achievementplates.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
//    @Inject(method = "onBlockEntityUpdate",
//            at = @At(value = "INVOKE_ASSIGN",
//                target = "Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;getBlockEntityType()I",
//                shift = At.Shift.AFTER),
//            locals = LocalCapture.CAPTURE_FAILSOFT,
//            cancellable = false)
//    private void mixinOnBlockEntityUpdate(BlockEntityUpdateS2CPacket packet,CallbackInfo ci, BlockPos blockPos, BlockEntity blockEntity, int i){
//        if(i == PlateBlockEntity.PLATE_BLOCK_ENTITY_ID && blockEntity instanceof PlateBlockEntity entity){
//            entity.readNbt(packet.getNbt());
//        }
//    }
}
