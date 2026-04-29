package eu.cj4.declarativeui.api.menu.slot.action;

import com.mojang.serialization.MapCodec;

public record ClickActionType(MapCodec<? extends ClickAction> codec) {
}
