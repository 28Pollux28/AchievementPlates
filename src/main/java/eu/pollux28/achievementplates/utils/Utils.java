package eu.pollux28.achievementplates.utils;

import eu.pollux28.achievementplates.init.ModBlocks;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class Utils {

    @NotNull
    public static ItemStack getPlateItemStack(Text playerName, AdvancementDisplay display) {
        NbtCompound nbt1 = new NbtCompound();
        NbtCompound nbt2 = new NbtCompound();
        writeDisplayToNbt(nbt2, display);
        nbt2.putString("player_name", playerName.asString());
        nbt1.put("BlockEntityTag", nbt2);
        ItemStack itemStack = ModBlocks.PLATE_ITEM.getDefaultStack();
        itemStack.setTag(nbt1);
        itemStack.setCustomName(playerName.shallowCopy().setStyle(Style.EMPTY.withColor(Formatting.AQUA)).append(new LiteralText("'s ").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(display.getTitle().shallowCopy().setStyle(Style.EMPTY.withColor(display.getFrame().getTitleFormat()))).append(new LiteralText(" trophy plate").setStyle(Style.EMPTY.withColor(Formatting.GRAY))));
        return itemStack;
    }
    public static void writeDisplayToNbt(NbtCompound nbt, AdvancementDisplay display){
        NbtCompound nbtCompound = new NbtCompound();
        if(display.getTitle() instanceof TranslatableText title){
            nbtCompound.putString("display_title",title.getKey());
        }else{
            nbtCompound.putString("display_title",display.getTitle().asString());
        }
        if(display.getDescription() instanceof TranslatableText desc){
            nbtCompound.putString("display_desc",desc.getKey());
        }else{
            nbtCompound.putString("display_desc",display.getDescription().asString());
        }

        if(display.getBackground()!=null){
            nbtCompound.putString("display_background",display.getBackground().toString());
        }
        nbtCompound.put("display_icon",display.getIcon().writeNbt(new NbtCompound()));
        nbtCompound.putString("display_frame_id", display.getFrame().getId());
        nbtCompound.putBoolean("display_toast", display.shouldShowToast());
        nbtCompound.putBoolean("display_chat", display.shouldAnnounceToChat());
        nbtCompound.putBoolean("display_hidden", display.isHidden());
        nbt.put("display", nbtCompound);
    }
}
