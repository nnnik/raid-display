package de.nnnik.raiddisplay.mixin;

import io.netty.buffer.Unpooled;
import java.util.Collection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.raid.Raid;

@Mixin(DebugInfoSender.class)
public abstract class DebugRendererInfoManagerMixin {
	
	@Inject(at = @At("HEAD"), method = "sendRaids")
	private static void sendRaids(ServerWorld world, Collection<Raid> raids, CallbackInfo ci) {
		PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
		packetByteBuf.writeInt(raids.size());
		for (Raid r : raids) {
			packetByteBuf.writeBlockPos(r.getCenter());
		}
		
		Packet<?> packet = new CustomPayloadS2CPacket(CustomPayloadS2CPacket.DEBUG_RAIDS, packetByteBuf);
		for (PlayerEntity player : world.toServerWorld().getPlayers()) {
			((ServerPlayerEntity)player).networkHandler.sendPacket(packet);
		}
	}
}
