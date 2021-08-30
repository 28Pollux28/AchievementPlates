package eu.pollux28.achievementplates.client.render;

import eu.pollux28.achievementplates.block.entity.renderer.PlateBlockEntityRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;

public class PlateRenderLayer extends RenderLayer {
    public PlateRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);

    }
    public static RenderLayer createPlateLayer(){
        return of("plate", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(RenderPhase.CUTOUT_MIPPED_SHADER).texture(Textures.create().add(PlateBlockEntityRenderer.WIDGETS_TEXTURE, false, true).build()).lightmap(RenderPhase.ENABLE_LIGHTMAP).build(false));

    }
}
