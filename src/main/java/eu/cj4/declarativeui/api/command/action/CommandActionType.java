package eu.cj4.declarativeui.api.command.action;

import com.mojang.serialization.MapCodec;

public record CommandActionType(MapCodec<? extends CommandAction> codec) {
}
