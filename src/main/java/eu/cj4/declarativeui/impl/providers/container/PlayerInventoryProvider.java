package eu.cj4.declarativeui.impl.providers.container;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.providers.ContainerProvider;
import eu.cj4.declarativeui.api.providers.ContainerProviderType;
import eu.cj4.declarativeui.impl.providers.ContainerProviders;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public record PlayerInventoryProvider() implements ContainerProvider {
    public static final MapCodec<PlayerInventoryProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.stable(new PlayerInventoryProvider()));

    @Override
    public ContainerProviderType getType() {
        return ContainerProviders.PLAYER_INVENTORY;
    }

    @Override
    public Container getContainer(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getInventory();
        }
        return null;
    }
}
