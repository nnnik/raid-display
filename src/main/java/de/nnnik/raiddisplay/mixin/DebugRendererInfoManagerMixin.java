package de.nnnik.raiddisplay.mixin;

import io.netty.buffer.Unpooled;
import java.util.Collection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.DebugRendererInfoManager;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.PacketByteBuf;

@Mixin(DebugRendererInfoManager.class)
public class DebugRendererInfoManagerMixin {
	
	@Inject(at = @At("HEAD"), method = "sendRaids")
	private static void sendRaids(ServerWorld world, Collection<Raid> raids, CallbackInfo ci) {
		PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
		packetByteBuf.writeInt(raids.size());
		for (Raid r : raids) {
			packetByteBuf.writeBlockPos(r.getCenter());
		}
		
		Packet<?> packet = new CustomPayloadS2CPacket(CustomPayloadS2CPacket.DEBUG_RAIDS, packetByteBuf);
		for (PlayerEntity player : world.getWorld().getPlayers()) {
			((ServerPlayerEntity)player).networkHandler.sendPacket(packet);
		}
	}
}
