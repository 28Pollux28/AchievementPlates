package eu.pollux28.achievementplates.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class WallPlateBlock extends AbstractPlateBlock{
    public WallPlateBlock(Settings settings) {
        super(settings);
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);

        switch (dir){
            case NORTH:
//                VoxelShape bottom = Block.createCuboidShape(3.0, 0,7.0,13.0,1.0,10.0);
                VoxelShape plate = Block.createCuboidShape(3.0,3,15.0,13.0,13,16.0);
                return plate;
            case SOUTH:
//                bottom = Block.createCuboidShape(3.0, 0,6.0,13.0,1.0,9.0);
                plate = Block.createCuboidShape(3.0,3,0.0,13.0,13,1.0);
                return plate;
            case WEST:
//                bottom = Block.createCuboidShape(7.0, 0,3.0,10.0,1.0,13.0);
                plate = Block.createCuboidShape(15.0,3,3.0,16.0,13,13.0);
                return plate;
            case EAST:
//                bottom = Block.createCuboidShape(6.0, 0,3.0,9.0,1.0,13.0);
                plate = Block.createCuboidShape(0.0,3,3.0,1.0,13,13.0);
                return plate;
            default:return VoxelShapes.fullCube();
        }
    }
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.offset(((Direction)state.get(FACING)).getOpposite())).getMaterial().isSolid();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        WorldView worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction[] directions = ctx.getPlacementDirections();
        Direction[] var7 = directions;
        int var8 = directions.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            Direction direction = var7[var9];
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = (BlockState)blockState.with(FACING, direction2);
                if (blockState.canPlaceAt(worldView, blockPos)) {
                    return (BlockState)blockState;
                }
            }
        }

        return null;
    }
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
