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

public class PlateBlock extends AbstractPlateBlock {

    //public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;


    public PlateBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
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

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !this.canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

}
