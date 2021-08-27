package eu.pollux28.achievementplates.block;

import com.google.common.collect.ImmutableMap;
import eu.pollux28.achievementplates.block.entity.PlateBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
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
            case SOUTH:
                VoxelShape bottom = Block.createCuboidShape(3.0, 0,7.0,13.0,1.0,10.0);
                VoxelShape plate = Block.createCuboidShape(3.0,1,8.0,13.0,10,9.0);
                return VoxelShapes.union(bottom,plate);
            case WEST:
            case EAST:
                bottom = Block.createCuboidShape(7.0, 0,3.0,10.0,1.0,13.0);
                plate = Block.createCuboidShape(8.0,1,3.0,9.0,10,13.0);
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
                }
            }
            if (placer != null)
                plateBlockEntity.setPlayerName(placer.getName().asString());
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
    }
}
