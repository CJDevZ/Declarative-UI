package eu.cj4.declarativeui.mixin;

import eu.cj4.declarativeui.impl.customclickaction.DeclaredCustomClickAction;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public class ServerCommonPacketListenerImplMixin {
    @Inject(method = "handleCustomClickAction", at = @At("TAIL"))
    private void handleCustomClickAction(ServerboundCustomClickActionPacket packet, CallbackInfo ci) {
        if (!((ServerCommonPacketListenerImpl) (Object) this instanceof ServerGamePacketListenerImpl gamePacketListener)) {
            return;
        }
        ServerPlayer serverPlayer = gamePacketListener.getPlayer();
        DeclaredCustomClickAction action = serverPlayer.registryAccess()
                .lookupOrThrow(DeclarativeUIRegistries.CUSTOM_CLICK_ACTION)
                .getValue(packet.id());

        if (action == null) {
            return;
        }

        CompoundTag payload = packet.payload()
                .map(tag -> {
                    if (tag instanceof CompoundTag compoundTag) {
                        return compoundTag;
                    }

                    CompoundTag wrapped = new CompoundTag();
                    wrapped.put("payload", tag);
                    return wrapped;
                })
                .orElse(null);

        action.run(serverPlayer, payload);
    }
}
