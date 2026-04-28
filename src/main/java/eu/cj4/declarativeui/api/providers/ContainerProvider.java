package eu.cj4.declarativeui.api.providers;

import eu.cj4.declarativeui.api.container.DeclaredContainer;
import eu.cj4.declarativeui.impl.providers.container.ContainerEntityProvider;
import eu.cj4.declarativeui.impl.providers.container.EnderChestProvider;
import eu.cj4.declarativeui.impl.providers.container.PlayerContainerProvider;
import eu.cj4.declarativeui.impl.providers.container.PlayerInventoryProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public interface ContainerProvider {
    ContainerProviderType getType();

    Container getContainer(Entity entity);

    static ContainerEntityProvider containerEntity() {
        return new ContainerEntityProvider();
    }

    static EnderChestProvider enderChest() {
        return new EnderChestProvider();
    }

    static PlayerContainerProvider playerContainer(ResourceKey<DeclaredContainer> resourceKey) {
        return new PlayerContainerProvider(resourceKey);
    }

    static PlayerInventoryProvider playerInventory() {
        return new PlayerInventoryProvider();
    }
}
