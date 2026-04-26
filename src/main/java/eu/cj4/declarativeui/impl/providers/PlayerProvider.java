package eu.cj4.declarativeui.impl.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.NamespacedContainerHolder;
import eu.cj4.declarativeui.api.providers.ContainerProvider;
import eu.cj4.declarativeui.api.providers.ContainerProviderType;
import net.minecraft.server.level.ServerPlayer;

public record PlayerProvider() implements ContainerProvider {
    public static final MapCodec<PlayerProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.stable(new PlayerProvider()));

    @Override
    public ContainerProviderType getType() {
        return ContainerProviders.PLAYER;
    }

    @Override
    public NamespacedContainerHolder getNamespacedContainerHolder(ServerPlayer serverPlayer) {
        return (NamespacedContainerHolder) serverPlayer;
    }
}
