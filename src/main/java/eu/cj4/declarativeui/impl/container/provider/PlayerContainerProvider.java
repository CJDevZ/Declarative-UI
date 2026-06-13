package eu.cj4.declarativeui.impl.container.provider;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.container.NamespacedContainerHolder;
import eu.cj4.declarativeui.impl.container.DeclaredContainer;
import eu.cj4.declarativeui.api.container.ContainerProvider;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public record PlayerContainerProvider(ResourceKey<DeclaredContainer> container) implements ContainerProvider {
    public static final MapCodec<PlayerContainerProvider> MAP_CODEC = ResourceKey.codec(DeclarativeUIRegistries.CONTAINER).fieldOf("container").xmap(PlayerContainerProvider::new, PlayerContainerProvider::container);

    @Override
    public MapCodec<PlayerContainerProvider> codec() {
        return MAP_CODEC;
    }

    @Override
    public Container getContainer(Entity entity) {
        if (entity instanceof NamespacedContainerHolder containerHolder) {
            return containerHolder.declarative_ui$namespacedContainer(this.container.identifier());
        }
        return null;
    }
}
