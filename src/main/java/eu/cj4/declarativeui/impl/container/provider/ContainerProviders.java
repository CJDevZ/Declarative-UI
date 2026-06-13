package eu.cj4.declarativeui.impl.container.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.container.ContainerProvider;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public final class ContainerProviders {
    public static final Codec<ContainerProvider> TYPED_CODEC = DeclarativeUIBuiltInRegistries.CONTAINER_PROVIDER_TYPE.byNameCodec().dispatch(ContainerProvider::codec, c -> c);

    private static void register(String name, MapCodec<? extends ContainerProvider> mapCodec) {
        Registry.register(DeclarativeUIBuiltInRegistries.CONTAINER_PROVIDER_TYPE, Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
        register("container_entity", ContainerEntityProvider.MAP_CODEC);
        register("ender_chest", EnderChestProvider.MAP_CODEC);
        register("player_container", PlayerContainerProvider.MAP_CODEC);
        register("player_inventory", PlayerInventoryProvider.MAP_CODEC);
    }
}
