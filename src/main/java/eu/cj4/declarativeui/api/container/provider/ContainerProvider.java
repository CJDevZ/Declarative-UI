package eu.cj4.declarativeui.api.container.provider;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.container.DeclaredContainer;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.impl.container.provider.ContainerEntityProvider;
import eu.cj4.declarativeui.impl.container.provider.EnderChestProvider;
import eu.cj4.declarativeui.impl.container.provider.PlayerContainerProvider;
import eu.cj4.declarativeui.impl.container.provider.PlayerInventoryProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

    static ContainerProviderType register(ResourceLocation id, MapCodec<? extends ContainerProvider> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CONTAINER_PROVIDER_TYPE, id, new ContainerProviderType(mapCodec));
    }
}
