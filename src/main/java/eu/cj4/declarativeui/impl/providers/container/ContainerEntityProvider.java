package eu.cj4.declarativeui.impl.providers.container;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.providers.ContainerProvider;
import eu.cj4.declarativeui.api.providers.ContainerProviderType;
import eu.cj4.declarativeui.impl.providers.ContainerProviders;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public record ContainerEntityProvider() implements ContainerProvider {
    public static final MapCodec<ContainerEntityProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.stable(new ContainerEntityProvider()));

    @Override
    public ContainerProviderType getType() {
        return ContainerProviders.CONTAINER_ENTITY;
    }

    @Override
    public Container getContainer(Entity entity) {
        if (entity instanceof Container) {
            return (Container) entity;
        }
        return null;
    }
}
