package eu.cj4.declarativeui.api.command.action;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public record CommandActionType(MapCodec<? extends CommandAction> codec) {
    public static CommandActionType register(ResourceLocation id, MapCodec<? extends CommandAction> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.COMMAND_ACTION_TYPE, id, new CommandActionType(mapCodec));
    }
}
