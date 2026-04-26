package eu.cj4.declarativeui.impl.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.providers.ContainerProvider;
import eu.cj4.declarativeui.api.providers.ContainerProviderType;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.api.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ContainerProviders {
    public static final Codec<ContainerProvider> TYPED_CODEC;
    public static final ContainerProviderType PLAYER;

    private static ContainerProviderType register(String name, MapCodec<? extends ContainerProvider> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CONTAINER_PROVIDER_TYPE, ResourceLocation.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), new ContainerProviderType(mapCodec));
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.CONTAINER_PROVIDER_TYPE.byNameCodec().dispatch(ContainerProvider::getType, ContainerProviderType::codec);
        PLAYER = register("player", PlayerProvider.CODEC);
    }
}
