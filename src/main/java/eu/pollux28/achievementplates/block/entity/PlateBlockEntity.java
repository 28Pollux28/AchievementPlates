package eu.pollux28.achievementplates.block.entity;

import eu.pollux28.achievementplates.block.PlateBlock;
import eu.pollux28.achievementplates.init.ModBlocks;
import eu.pollux28.achievementplates.utils.Utils;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PlateBlockEntity extends BlockEntity implements Nameable {
    public static int PLATE_BLOCK_ENTITY_ID = 14587;
    private AdvancementDisplay display= new AdvancementDisplay(new ItemStack((Items.BARRIER.asItem())), Text.translatable("advancement_plates.notitle"), Text.translatable("advancement_plates.nodesc"), (Identifier)null, AdvancementFrame.TASK   , true, true, false);
    private String playerName = "";
    private Text customName;

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
    public boolean shouldRenderFrame(Direction dir){
        return this.getCachedState().get(PlateBlock.FACING).getAxis().equals(dir.getAxis());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.display = readDisplayFromNBT(nbt.getCompound("display"));
        this.playerName = nbt.getString("player_name");
        if (nbt.contains("CustomName", 8)) {
            this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeDisplayToNbt(nbt);
        nbt.putString("player_name", playerName);
        if (this.customName != null) {
            nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
        }
    }

    private void writeDisplayToNbt(NbtCompound nbt){
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("display_title",((TranslatableTextContent)display.getTitle().getContent()).getKey());
        nbtCompound.putString("display_desc",((TranslatableTextContent)display.getDescription().getContent()).getKey());
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
        Text title = Text.translatable(nbt.getString("display_title"));
        Text desc = Text.translatable(nbt.getString("display_desc"));
        Identifier background = nbt.contains("display_background") ? new Identifier(nbt.getString("display_background")) : null;
        ItemStack icon = ItemStack.fromNbt(nbt.getCompound("display_icon"));
        AdvancementFrame frame = AdvancementFrame.forName(nbt.getString("display_frame_id"));
        boolean displayToast = nbt.getBoolean("display_toast");
        boolean displayChat = nbt.getBoolean("display_chat");
        boolean displayHidden = nbt.getBoolean("display_hidden");
        return new AdvancementDisplay(icon,title,desc,background,frame,displayToast,displayChat,displayHidden);
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }


    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Text text = getCustomName();
        if(text!=null){
            player.sendMessage(Text.literal("Advancement ").setStyle(Style.EMPTY.withColor(Formatting.GRAY)).append(Texts.setStyleIfAbsent((MutableText) display.getTitle(), Style.EMPTY.withColor(display.getFrame().getTitleFormat()))).append(Text.literal(" was obtained by ")).append(Text.literal(playerName).setStyle(Style.EMPTY.withColor(Formatting.AQUA))),true);
            player.sendMessage(Text.translatable("chat.type.advancement." + this.display.getFrame().getId(), Text.literal(this.playerName).setStyle(Style.EMPTY.withColor(Formatting.AQUA)),display.getTitle()),true);
        }else{
            player.sendMessage(Text.literal("This trophy was not obtained !").setStyle(Style.EMPTY.withColor(Formatting.RED)),false);
        }
        return ActionResult.CONSUME;
    }

    public ItemStack getPickStack() {
        return Utils.getPlateItemStack(Text.literal(playerName), this.display);
    }

    public void setCustomName(Text customName) {
        this.customName = customName;
    }

    public Text getName() {
        return this.customName != null ? this.customName : Text.translatable("blocks."+ BlockEntityType.getId(this.getType()));
    }

    public Text getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Text getCustomName() {
        return this.customName;
    }
}
