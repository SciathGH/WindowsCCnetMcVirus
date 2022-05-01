package windowspcvirus.windowspcvirus.map;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import windowspcvirus.windowspcvirus.JustMap;
import windowspcvirus.windowspcvirus.windowspcvirusutil.ImageUtil;
import windowspcvirus.windowspcvirus.windowspcvirusutil.SpriteAtlas;
import windowspcvirus.windowspcvirus.windowspcvirusutil.colors.Colors;
import windowspcvirus.windowspcvirus.windowspcvirusutil.render.RenderUtil;

public class DirectionArrow extends Sprite {
	private final static VertexFormat vertexFormat = new VertexFormat(ImmutableMap.of("postition", VertexFormats.POSITION_ELEMENT, "texture", VertexFormats.TEXTURE_0_ELEMENT, "normal", VertexFormats.NORMAL_ELEMENT, "padding", VertexFormats.PADDING_ELEMENT));
	private static DirectionArrow ARROW;

	private DirectionArrow(Identifier texture, int w, int h) {
		super(SpriteAtlas.MAP_ICONS, new Sprite.Info(texture, w, h, AnimationResourceMetadata.EMPTY), 0, w, h, 0, 0, ImageUtil.loadImage(texture, w, h));
	}


	private static void addVertices(Matrix4f m4f, Matrix3f m3f, VertexConsumer vertexConsumer, int size) {
		float half = size / 2;

		vertexConsumer.vertex(m4f, -half, -half, 0.0F).texture(0.0F, 0.0F).normal(m3f, 0.0F, 1.0F, 0.0F).next();
		vertexConsumer.vertex(m4f, -half, half, 0.0F).texture(0.0F, 1.0F).normal(m3f, 0.0F, 1.0F, 0.0F).next();
		vertexConsumer.vertex(m4f, half, half, 0.0F).texture(1.0F, 1.0F).normal(m3f, 0.0F, 1.0F, 0.0F).next();
		vertexConsumer.vertex(m4f, half, -half, 0.0F).texture(1.0F, 0.0F).normal(m3f, 0.0F, 1.0F, 0.0F).next();
	}

}
