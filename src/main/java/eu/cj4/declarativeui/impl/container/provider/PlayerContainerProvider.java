package eu.cj4.declarativeui.impl.container.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.container.NamespacedContainerHolder;
import eu.cj4.declarativeui.impl.container.DeclaredContainer;
import eu.cj4.declarativeui.api.container.provider.ContainerProvider;
import eu.cj4.declarativeui.api.container.provider.ContainerProviderType;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public record PlayerContainerProvider(ResourceKey<DeclaredContainer> container) implements ContainerProvider {
    public static final MapCodec<PlayerContainerProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(DeclarativeUIRegistries.CONTAINER).fieldOf("container").forGetter(PlayerContainerProvider::container)
    ).apply(instance, PlayerContainerProvider::new));

    @Override
    public ContainerProviderType getType() {
        return ContainerProviders.PLAYER_CONTAINER;
    }

    @Override
    public Container getContainer(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            return ((NamespacedContainerHolder) serverPlayer).declarative_ui$namespacedContainer(this.container.location());
        }
        return null;
    }
}
