package eu.cj4.declarativeui.impl.menu.slot.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.SlotProvider;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public final class SlotProviders {
    public static final Codec<SlotProvider> TYPED_CODEC = DeclarativeUIBuiltInRegistries.SLOT_PROVIDER_TYPE.byNameCodec().dispatch(SlotProvider::codec, c -> c);

    private static void register(String name, MapCodec<? extends SlotProvider> mapCodec) {
        Registry.register(DeclarativeUIBuiltInRegistries.SLOT_PROVIDER_TYPE, Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
        register("simple", SimpleProvider.MAP_CODEC);
        register("tag", TagProvider.MAP_CODEC);
        register("animated", AnimatedProvider.MAP_CODEC);
        register("empty", EmptyProvider.MAP_CODEC);
    }
}
