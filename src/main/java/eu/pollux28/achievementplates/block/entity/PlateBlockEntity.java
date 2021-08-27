package eu.pollux28.achievementplates.block.entity;

import eu.pollux28.achievementplates.init.ModBlocks;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PlateBlockEntity extends BlockEntity {
    private AdvancementDisplay display= new AdvancementDisplay(new ItemStack((Items.ACACIA_BOAT)), new TranslatableText("advancements.adventure.very_very_frightening.title"), new TranslatableText("advancements.adventure.very_very_frightening.description"), (Identifier)null, AdvancementFrame.TASK, true, true, false);
    private String playerName = "";

    public PlateBlockEntity(BlockPos pos, BlockState state, String playerName, Advancement advancement){
        this(pos, state);
        this.playerName = playerName;
        this.display = advancement.getDisplay();
    }

    public PlateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.PLATE_BLOCK_ENTITY, pos, state);
    }

    public AdvancementDisplay getDisplay() {
        return display;
    }

    public void setDisplay(AdvancementDisplay display) {
        this.display = display;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.display = readDisplayFromNBT(nbt.getCompound("display"));
        this.playerName = nbt.getString("player_name");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeDisplayToNbt(nbt);
        nbt.putString("player_name", playerName);
        return nbt;
    }


    private void writeDisplayToNbt(NbtCompound nbt){
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("display_title",((TranslatableText)display.getTitle()).getKey());
        nbtCompound.putString("display_desc",((TranslatableText)display.getDescription()).getKey());
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
    private AdvancementDisplay readDisplayFromNBT(NbtCompound nbt){
        TranslatableText title = new TranslatableText(nbt.getString("display_title"));
        TranslatableText desc = new TranslatableText(nbt.getString("display_desc"));
        Identifier background = nbt.contains("display_background") ? new Identifier(nbt.getString("display_background")) : null;
        ItemStack icon = ItemStack.fromNbt(nbt.getCompound("display_icon"));
        AdvancementFrame frame = AdvancementFrame.forName(nbt.getString("display_frame_id"));
        boolean displayToast = nbt.getBoolean("display_toast");
        boolean displayChat = nbt.getBoolean("display_chat");
        boolean displayHidden = nbt.getBoolean("display_hidden");
        return new AdvancementDisplay(icon,title,desc,background,frame,displayToast,displayChat,displayHidden);
    }
}
