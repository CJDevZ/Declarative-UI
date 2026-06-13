package eu.cj4.declarativeui.api.container;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.container.DeclaredContainer;
import eu.cj4.declarativeui.impl.container.provider.ContainerEntityProvider;
import eu.cj4.declarativeui.impl.container.provider.EnderChestProvider;
import eu.cj4.declarativeui.impl.container.provider.PlayerContainerProvider;
import eu.cj4.declarativeui.impl.container.provider.PlayerInventoryProvider;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public interface ContainerProvider {
    MapCodec<? extends ContainerProvider> codec();

    Container getContainer(Entity entity);

    static ContainerEntityProvider containerEntity() {
        return ContainerEntityProvider.INSTANCE;
    }

    static EnderChestProvider enderChest() {
        return EnderChestProvider.INSTANCE;
    }

    static PlayerContainerProvider playerContainer(ResourceKey<DeclaredContainer> resourceKey) {
        return new PlayerContainerProvider(resourceKey);
    }

    static PlayerInventoryProvider playerInventory() {
        return PlayerInventoryProvider.INSTANCE;
    }

    static <T extends ContainerProvider> MapCodec<T> register(Identifier id, MapCodec<T> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CONTAINER_PROVIDER_TYPE, id, mapCodec);
    }
}
