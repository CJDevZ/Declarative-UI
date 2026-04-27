package eu.cj4.declarativeui.api.menu.slot.provider;

import com.mojang.serialization.MapCodec;

public record SlotProviderType(MapCodec<? extends SlotProvider> codec) {
}
