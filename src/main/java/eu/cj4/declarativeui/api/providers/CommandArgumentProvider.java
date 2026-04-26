package eu.cj4.declarativeui.api.providers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.datafixers.util.Either;
import net.minecraft.commands.CommandBuildContext;

import java.util.function.Function;
import java.util.function.Supplier;

public record CommandArgumentProvider(Either<Supplier<ArgumentType<?>>, Function<CommandBuildContext, ArgumentType<?>>> argumentType) {
    public ArgumentType<?> apply(CommandBuildContext buildContext) {
        return argumentType.map(Supplier::get, function -> function.apply(buildContext));
    }
}
