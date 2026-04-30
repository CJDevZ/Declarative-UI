package eu.cj4.declarativeui.impl.container.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.container.provider.ContainerProvider;
import eu.cj4.declarativeui.api.container.provider.ContainerProviderType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public record EnderChestProvider() implements ContainerProvider {
    public static final MapCodec<EnderChestProvider> CODEC = RecordCodecBuilder.build(RecordCodecBuilder.stable(new EnderChestProvider()));

    @Override
    public ContainerProviderType getType() {
        return ContainerProviders.ENDER_CHEST;
    }

    @Override
    public Container getContainer(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getEnderChestInventory();
        }
        return null;
    }
}
