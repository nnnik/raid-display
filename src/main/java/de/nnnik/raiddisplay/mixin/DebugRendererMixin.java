package de.nnnik.raiddisplay.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.debug.RaidCenterDebugRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
	
	@Shadow
	public RaidCenterDebugRenderer raidCenterDebugRenderer;
	
	@Inject(at = @At("RETURN"), method = "render")
	public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
		this.raidCenterDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
	}
}
