package eu.pollux28.achievementplates.mixins;

import eu.pollux28.achievementplates.predicate.CustomNBTPredicate;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemPredicate.Builder.class)
public class ItemPredicateBuilderMixin {
    @Shadow
    private NbtPredicate nbt;

    @Inject(method = "Lnet/minecraft/predicate/item/ItemPredicate$Builder;nbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/predicate/item/ItemPredicate$Builder;", at= @At("TAIL"))
    private void injectBuilderNBT(NbtCompound nbt, CallbackInfoReturnable<ItemPredicate.Builder> cir){
        this.nbt = new CustomNBTPredicate(nbt);
    }
}
