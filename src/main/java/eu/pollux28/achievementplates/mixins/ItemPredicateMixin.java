package eu.pollux28.achievementplates.mixins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.pollux28.achievementplates.predicate.CustomNBTPredicate;
import net.minecraft.potion.Potion;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

@Mixin(ItemPredicate.class)
public class ItemPredicateMixin {
    @Inject(method = "Lnet/minecraft/predicate/item/ItemPredicate;fromJson(Lcom/google/gson/JsonElement;)Lnet/minecraft/predicate/item/ItemPredicate;", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void injectFromJSON(JsonElement el, CallbackInfoReturnable<ItemPredicate> cir, JsonObject jsonObject, NumberRange.IntRange intRange, NumberRange.IntRange intRange2, NbtPredicate nbtPredicate, Set set, JsonArray jsonArray, Tag tag, Potion potion, EnchantmentPredicate enchantmentPredicates[], EnchantmentPredicate enchantmentPredicates2[]){
        if(cir.getReturnValue()==ItemPredicate.ANY){
            cir.setReturnValue(ItemPredicate.ANY);
        }else{
            CustomNBTPredicate nbtPredicat = (CustomNBTPredicate) CustomNBTPredicate.fromJson(jsonObject.get("nbt"));
            cir.setReturnValue(new ItemPredicate(tag,set,intRange,intRange2,enchantmentPredicates, enchantmentPredicates2,potion,nbtPredicat));
        }
    }
}
