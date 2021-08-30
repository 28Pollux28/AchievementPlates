package eu.pollux28.achievementplates.predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pollux28.achievementplates.AchievementPlates;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

public class CustomNBTPredicate extends NbtPredicate{

    public static CustomNBTPredicate ANY = new CustomNBTPredicate((NbtCompound)null);
    private final NbtCompound nbt;

    public CustomNBTPredicate(@Nullable NbtCompound nbt) {
        super(nbt);
        this.nbt = nbt;
    }

    @Override
    public boolean test(@Nullable NbtElement element) {
        if(this.nbt !=null && this.nbt.getSize()==0){
            if(element == null){
                return true;
            }else{
                return false;
            }
        }
        if (element == null) {
            return this == ANY;
        } else {
            return this.nbt == null || NbtHelper.matches(this.nbt, element, true);
        }
    }
    public static CustomNBTPredicate fromJson(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            NbtCompound nbtCompound2;
            try {
                nbtCompound2 = StringNbtReader.parse(JsonHelper.asString(json, "nbt"));
            } catch (CommandSyntaxException var3) {
                throw new JsonSyntaxException("Invalid nbt tag: " + var3.getMessage());
            }

            return new CustomNBTPredicate(nbtCompound2);
        } else {
            return ANY;
        }
    }
}
