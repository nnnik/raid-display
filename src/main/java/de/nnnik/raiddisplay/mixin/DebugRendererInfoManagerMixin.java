package de.nnnik.raiddisplay.mixin;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(DebugInfoSender.class)
public abstract class DebugRendererInfoManagerMixin {

    @Inject(at = @At("HEAD"), method = "sendRaids")
    private static void sendRaids(final ServerWorld world, final Collection<Raid> raids, final CallbackInfo ci) {
        final PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeInt(raids.size());
        for (final Raid r : raids) {
            packetByteBuf.writeBlockPos(r.getCenter());
        }

        final Packet<?> packet = new CustomPayloadS2CPacket(CustomPayloadS2CPacket.DEBUG_RAIDS, packetByteBuf);
        for (final PlayerEntity player : world.toServerWorld().getPlayers()) {
            ((ServerPlayerEntity) player).networkHandler.sendPacket(packet);
        }
    }
}
