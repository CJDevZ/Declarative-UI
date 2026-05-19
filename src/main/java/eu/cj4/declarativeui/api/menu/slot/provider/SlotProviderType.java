package eu.cj4.declarativeui.api.menu.slot.provider;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public record SlotProviderType(MapCodec<? extends SlotProvider> codec) {
    public static SlotProviderType register(ResourceLocation id, MapCodec<? extends SlotProvider> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.SLOT_PROVIDER_TYPE, id, new SlotProviderType(mapCodec));
    }
}
