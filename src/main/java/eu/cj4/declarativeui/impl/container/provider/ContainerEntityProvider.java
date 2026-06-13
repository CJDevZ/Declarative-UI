package eu.cj4.declarativeui.impl.container.provider;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.container.ContainerProvider;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public record ContainerEntityProvider() implements ContainerProvider {
    public static final ContainerEntityProvider INSTANCE = new ContainerEntityProvider();
    public static final MapCodec<ContainerEntityProvider> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<ContainerEntityProvider> codec() {
        return MAP_CODEC;
    }

    @Override
    public Container getContainer(Entity entity) {
        if (entity instanceof Container) {
            return (Container) entity;
        }
        return null;
    }
}
