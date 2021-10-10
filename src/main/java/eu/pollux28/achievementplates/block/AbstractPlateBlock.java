package eu.pollux28.achievementplates.block;

import com.google.common.collect.ImmutableMap;
import eu.pollux28.achievementplates.block.entity.PlateBlockEntity;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class AbstractPlateBlock extends HorizontalFacingBlock implements BlockEntityProvider {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;


    public AbstractPlateBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    protected ImmutableMap<BlockState, VoxelShape> getShapesForStates(Function<BlockState, VoxelShape> function) {
        return super.getShapesForStates(function);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntity(pos,state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        NbtCompound nbt = itemStack.getOrCreateTag();
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PlateBlockEntity plateBlockEntity) {

            if (nbt.contains("BlockEntityTag")) {
                NbtCompound nbtCompound2 = nbt.getCompound("BlockEntityTag");
                if (nbtCompound2.contains("display") && nbtCompound2.contains("player_name")) {
                    plateBlockEntity.readNbt(nbtCompound2);
                    AdvancementDisplay display = plateBlockEntity.getDisplay();
                    if(!plateBlockEntity.getPlayerName().equalsIgnoreCase("")){
                        plateBlockEntity.setCustomName(new LiteralText(plateBlockEntity.getPlayerName()).shallowCopy().setStyle(Style.EMPTY.withColor(Formatting.AQUA)).append(new LiteralText("'s ").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(display.getTitle().shallowCopy().setStyle(Style.EMPTY.withColor(display.getFrame().getTitleFormat()))).append(new LiteralText(" trophy plate").setStyle(Style.EMPTY.withColor(Formatting.GRAY))));
                    }
                }
            }
        }
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient){
            return ActionResult.SUCCESS;
        }else{
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PlateBlockEntity plateBlockEntity) {
                return plateBlockEntity.onUse(state, world, pos , player, hand, hit);
            }
            return ActionResult.CONSUME;
        }
    }


    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof PlateBlockEntity entity ? entity.getPickStack() : super.getPickStack(world, pos, state);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
    }
}
