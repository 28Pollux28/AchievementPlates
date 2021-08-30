package eu.pollux28.achievementplates.block;

import com.google.common.collect.ImmutableMap;
import eu.pollux28.achievementplates.block.entity.PlateBlockEntity;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.block.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
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
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PlateBlock extends HorizontalFacingBlock implements BlockEntityProvider {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;


    public PlateBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);

        switch (dir){
            case NORTH:
                VoxelShape bottom = Block.createCuboidShape(3.0, 0,7.0,13.0,1.0,10.0);
                VoxelShape plate = Block.createCuboidShape(3.0,1,8.0,13.0,10,9.0);
                return VoxelShapes.union(bottom,plate);
            case SOUTH:
                bottom = Block.createCuboidShape(3.0, 0,6.0,13.0,1.0,9.0);
                plate = Block.createCuboidShape(3.0,1,7.0,13.0,10,8.0);
                return VoxelShapes.union(bottom,plate);
            case WEST:
                bottom = Block.createCuboidShape(7.0, 0,3.0,10.0,1.0,13.0);
                plate = Block.createCuboidShape(8.0,1,3.0,9.0,10,13.0);
                return VoxelShapes.union(bottom,plate);
            case EAST:
                bottom = Block.createCuboidShape(6.0, 0,3.0,9.0,1.0,13.0);
                plate = Block.createCuboidShape(7.0,1,3.0,8.0,10,13.0);
                return VoxelShapes.union(bottom,plate);
            default:return VoxelShapes.fullCube();
        }
    }


    @Override
    protected ImmutableMap<BlockState, VoxelShape> getShapesForStates(Function<BlockState, VoxelShape> function) {
        return super.getShapesForStates(function);
    }
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntity(pos,state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        NbtCompound nbt = itemStack.getOrCreateNbt();
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
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
    }
}
