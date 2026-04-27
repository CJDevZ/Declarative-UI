package eu.cj4.declarativeui.api.providers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public interface ContainerProvider {
    ContainerProviderType getType();

    Container getContainer(Entity entity);
}
