package eu.cj4.declarativeui.api.command.argument;

import com.mojang.serialization.MapCodec;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record CommandArgumentType(ArgumentTypeInfo<?, ?> argumentTypeInfo, MapCodec<? extends CommandArgument<?>> codec) {
}
