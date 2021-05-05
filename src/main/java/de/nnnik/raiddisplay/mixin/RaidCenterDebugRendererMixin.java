package de.nnnik.raiddisplay.mixin;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.debug.RaidCenterDebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import de.nnnik.raiddisplay.BlendedBoxRenderer;

@Mixin(RaidCenterDebugRenderer.class)
public class RaidCenterDebugRendererMixin {
	
	@Shadow
	private Collection<BlockPos> raidCenters;
	@Shadow
	private MinecraftClient client;
	
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) { 
		RenderSystem.disableRescaleNormal();
		RenderSystem.disableLighting();
		RenderSystem.enableDepthTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(7425);
		BlockPos cameraPos = getCamera().getBlockPos();
		BlendedBoxRenderer.setColor(1f, 0f, 0f, 0.5f);
		BlendedBoxRenderer.setSize(0.5005); //bigger than 1 block to avoid z-fighting
		for (BlockPos raidCenter : raidCenters) {
			if (cameraPos.isWithinDistance(raidCenter, 160.0D)) {
	        	 drawRaidMarker(raidCenter);
			}
		}
		RenderSystem.shadeModel(7424);
		RenderSystem.enableLighting();
		RenderSystem.disableBlend();
		ci.cancel();
	}
	
	private static void drawRaidMarker(BlockPos atBlock) {
		RenderSystem.disableTexture();
		RenderSystem.disableAlphaTest();
		RenderSystem.enableBlend();
		RenderSystem.depthMask(false);
		drawBlendedBox(atBlock);
		drawString("Raid center "+atBlock.toShortString(), atBlock, -65536);
	}
	
	@SuppressWarnings("unused")
	private static void drawString(String string, BlockPos atBlock, int i) {
		double d = (double)atBlock.getX() + 0.5D;
		double e = (double)atBlock.getY() + 1.3D;
		double f = (double)atBlock.getZ() + 0.5D;
		DebugRenderer.drawString(string, d, e, f, i, 0.04F, true, 0.0F, true);
	}
	
	private static void drawBlendedBox(BlockPos atBlock) {
		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		Vec3d drawPosition = new Vec3d(atBlock.getX()+0.5,atBlock.getY()+0.5,atBlock.getZ()+0.5);
		if (camera.isReady()) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuffer();
			bufferBuilder.begin(4, VertexFormats.POSITION_COLOR);
			BlendedBoxRenderer.draw(drawPosition, camera.getPos(), bufferBuilder);
			tessellator.draw();
		}
	}
	
	private Camera getCamera() {
		return client.gameRenderer.getCamera();
	}
}
