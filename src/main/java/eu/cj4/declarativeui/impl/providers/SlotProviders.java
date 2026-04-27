package eu.cj4.declarativeui.impl.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProvider;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProviderType;
import eu.cj4.declarativeui.api.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.providers.slot.AnimatedProvider;
import eu.cj4.declarativeui.impl.providers.slot.SimpleProvider;
import eu.cj4.declarativeui.impl.providers.slot.TagProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class SlotProviders {
    public static final Codec<SlotProvider> TYPED_CODEC;
    public static final SlotProviderType SIMPLE;
    public static final SlotProviderType TAG;
    public static final SlotProviderType ANIMATED;

    private static SlotProviderType register(String name, MapCodec<? extends SlotProvider> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.SLOT_PROVIDER_TYPE, ResourceLocation.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), new SlotProviderType(mapCodec));
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.SLOT_PROVIDER_TYPE.byNameCodec().dispatch(SlotProvider::getType, SlotProviderType::codec);
        SIMPLE = register("simple", SimpleProvider.CODEC);
        TAG = register("tag", TagProvider.CODEC);
        ANIMATED = register("animated", AnimatedProvider.CODEC);
    }
}
