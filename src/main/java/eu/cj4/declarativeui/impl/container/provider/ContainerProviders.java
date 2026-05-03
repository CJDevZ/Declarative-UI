package eu.cj4.declarativeui.impl.container.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.container.provider.ContainerProvider;
import eu.cj4.declarativeui.api.container.provider.ContainerProviderType;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class ContainerProviders {
    public static final Codec<ContainerProvider> TYPED_CODEC;
    public static final ContainerProviderType CONTAINER_ENTITY;
    public static final ContainerProviderType ENDER_CHEST;
    public static final ContainerProviderType PLAYER_CONTAINER;
    public static final ContainerProviderType PLAYER_INVENTORY;

    private static ContainerProviderType register(String name, MapCodec<? extends ContainerProvider> mapCodec) {
        return ContainerProvider.register(Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.CONTAINER_PROVIDER_TYPE.byNameCodec().dispatch(ContainerProvider::getType, ContainerProviderType::codec);
        CONTAINER_ENTITY = register("container_entity", ContainerEntityProvider.CODEC);
        ENDER_CHEST = register("ender_chest", EnderChestProvider.CODEC);
        PLAYER_CONTAINER = register("player_container", PlayerContainerProvider.CODEC);
        PLAYER_INVENTORY = register("player_inventory", PlayerInventoryProvider.CODEC);
    }
}
