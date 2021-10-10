package eu.pollux28.achievementplates.block.entity.renderer;

import eu.pollux28.achievementplates.block.PlateBlock;
import eu.pollux28.achievementplates.block.entity.PlateBlockEntity;
import eu.pollux28.achievementplates.client.render.PlateRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class PlateBlockEntityRenderer implements BlockEntityRenderer<PlateBlockEntity> {
    public static final Identifier WIDGETS_TEXTURE = new Identifier("textures/gui/advancements/widgets.png");
    private final BlockEntityRenderDispatcher dispatcher;
    private TextRenderer textRenderer;
    public PlateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        this.textRenderer = ctx.getTextRenderer();
        this.dispatcher = ctx.getRenderDispatcher();
    }

    @Override
    public void render(PlateBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        BlockState blockState = entity.getCachedState();
        Direction direction = entity.getCachedState().get(PlateBlock.FACING);
        ItemStack itemStack = entity.getDisplay().getIcon();
        matrixStack.push();
        Matrix4f matrix4f = matrixStack.peek().getModel();
        if(blockState.getBlock() instanceof PlateBlock){
            this.renderSides(entity, matrix4f, vertexConsumerProvider.getBuffer(this.getLayer()),light);
            matrixStack.pop();

            matrixStack.push();
            float g = direction.asRotation();
            int id = direction.getId();
            switch (id) {
                case 2 -> matrixStack.translate(0.50D, 0.34375D, 0.53125D);
                case 3 -> matrixStack.translate(0.50D, 0.34375D, 0.46875D);
                case 4 -> matrixStack.translate(0.53125D, 0.34375D, 0.5D);
                case 5 -> matrixStack.translate(0.46875D, 0.34375D, 0.50D);
                default -> matrixStack.translate(0.50D,0.34375D, 0.53125D);
            }

            matrixStack.scale(0.25F, 0.25F, 0.25F);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180 -g));
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, light, overlay, matrixStack, vertexConsumerProvider, 54564451);
            matrixStack.pop();
        }else{
            this.renderSidesWall(entity, matrix4f, vertexConsumerProvider.getBuffer(this.getLayer()),light);
            matrixStack.pop();
            matrixStack.push();
            float g = direction.asRotation();
            int id = direction.getId();
            switch (id) {
                case 2 -> matrixStack.translate(0.50D, 0.5D, 0.96875D); //North
                case 3 -> matrixStack.translate(0.50D, 0.5D, 0.03125D); //South
                case 4 -> matrixStack.translate(0.96875D, 0.5D, 0.5D); //West
                case 5 -> matrixStack.translate(0.03125D, 0.5D, 0.50D); //East
                default -> matrixStack.translate(0.50D,0.5D, 0.96875D);
            }

            matrixStack.scale(0.25F, 0.25F, 0.25F);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180 -g));
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, light, overlay, matrixStack, vertexConsumerProvider, 54564451);
            matrixStack.pop();
        }

    }

    private void renderSides(PlateBlockEntity entity, Matrix4f matrix4f, VertexConsumer buffer,int light) {
        float zSouthOffset = entity.getCachedState().get(PlateBlock.FACING).equals(Direction.NORTH) ? 0.0625F:0;
        float xWestOffset = entity.getCachedState().get(PlateBlock.FACING).equals(Direction.WEST) ? 0.0625F:0;
        float u1 =entity.getDisplay().getFrame().getTextureV()/256F;
        float u2 =u1 +0.1015625F;
        float v1 =0.5F;
        float v2 =v1+0.1015625F;
        this.renderSide(entity, matrix4f, buffer, 0.3F, 0.7F, 0.125F+0.01875F, 0.5625F-0.01875F, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, u1,u2,v1,v2,Direction.SOUTH,light);
        this.renderSide(entity, matrix4f, buffer, 0.3F, 0.7F, 0.5625F-0.01875F, 0.125F+0.01875F, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset,u2,u1,v2,v1, Direction.NORTH, light);
        this.renderSide(entity, matrix4f, buffer, 0.46875F+xWestOffset, 0.46875F+xWestOffset, 0.5625F-0.01875F, 0.125F+0.01875F, 0.3F, 0.7F, 0.7F, 0.3F,u2,u1,v2,v1, Direction.EAST, light);
        this.renderSide(entity, matrix4f, buffer, 0.46875F+xWestOffset, 0.46875F+xWestOffset, 0.125F+0.01875F, 0.5625F-0.01875F, 0.3F, 0.7F, 0.7F, 0.3F, u1,u2,v1,v2,Direction.WEST, light);

    }

    private void renderSide(PlateBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4,float u1,float u2, float v1, float v2, Direction side,int light) {
        if (entity.shouldRenderFrame(side)) {
            vertices.vertex(model, x1, y1, z1).color(1.0F,1.0F,1.0F,1.0F).texture(u1,v2).light(light-25).next();
            vertices.vertex(model, x2, y1, z2).color(1.0F,1.0F,1.0F,1.0F).texture(u2,v2).light(light-25).next();
            vertices.vertex(model, x2, y2, z3).color(1.0F,1.0F,1.0F,1.0F).texture(u2,v1).light(light-25).next();
            vertices.vertex(model, x1, y2, z4).color(1.0F,1.0F,1.0F,1.0F).texture(u1,v1).light(light-25).next();
        }

    }

    private void renderSidesWall(PlateBlockEntity entity, Matrix4f matrix4f, VertexConsumer buffer,int light) {
        float zSouthOffset = entity.getCachedState().get(PlateBlock.FACING).equals(Direction.NORTH) ? 0.9375F:0;
        float xWestOffset = entity.getCachedState().get(PlateBlock.FACING).equals(Direction.WEST) ? 0.9375F:0;
        float u1 =entity.getDisplay().getFrame().getTextureV()/256F;
        float u2 =u1 +0.1015625F;
        float v1 =0.5F;
        float v2 =v1+0.1015625F;
        this.renderSideWall(entity, matrix4f, buffer, 0.3F, 0.7F, 0.125F+0.01875F+0.15625F, 0.5625F-0.01875F+0.15625F, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset, u1,u2,v1,v2,Direction.SOUTH,light);
        this.renderSideWall(entity, matrix4f, buffer, 0.3F, 0.7F, 0.5625F-0.01875F+0.15625F, 0.125F+0.01875F+0.15625F, 0.031255F+zSouthOffset, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset,u2,u1,v2,v1, Direction.NORTH, light);
        this.renderSideWall(entity, matrix4f, buffer, 0.03125F+xWestOffset, 0.03125F+xWestOffset, 0.5625F-0.01875F+0.15625F, 0.125F+0.01875F+0.15625F, 0.3F, 0.7F, 0.7F, 0.3F,u2,u1,v2,v1, Direction.EAST, light);
        this.renderSideWall(entity, matrix4f, buffer, 0.03125F+xWestOffset, 0.03125F+xWestOffset, 0.125F+0.01875F+0.15625F, 0.5625F-0.01875F+0.15625F, 0.3F, 0.7F, 0.7F, 0.3F, u1,u2,v1,v2,Direction.WEST, light);

    }

    private void renderSideWall(PlateBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4,float u1,float u2, float v1, float v2, Direction side,int light) {
        if (entity.shouldRenderFrame(side)) {
            vertices.vertex(model, x1, y1, z1).color(1.0F,1.0F,1.0F,1.0F).texture(u1,v2).light(light-25).next();
            vertices.vertex(model, x2, y1, z2).color(1.0F,1.0F,1.0F,1.0F).texture(u2,v2).light(light-25).next();
            vertices.vertex(model, x2, y2, z3).color(1.0F,1.0F,1.0F,1.0F).texture(u2,v1).light(light-25).next();
            vertices.vertex(model, x1, y2, z4).color(1.0F,1.0F,1.0F,1.0F).texture(u1,v1).light(light-25).next();
        }

    }


    private RenderLayer getLayer(){
        return PlateRenderLayer.createPlateLayer();
    }
}
