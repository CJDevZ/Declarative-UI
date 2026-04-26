package eu.cj4.declarativeui.impl.providers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import eu.cj4.declarativeui.api.providers.CommandArgumentProvider;
import eu.cj4.declarativeui.api.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public class CommandArgumentProviders {
    public static final Codec<CommandArgumentProvider> TYPED_CODEC;

    private static CommandArgumentProvider register(String string, Supplier<ArgumentType<?>> argumentType) {
        return Registry.register(DeclarativeUIBuiltInRegistries.COMMAND_ARGUMENT_PROVIDER, ResourceLocation.parse(string), new CommandArgumentProvider(Either.left(argumentType)));
    }

    private static CommandArgumentProvider register(String string, Function<CommandBuildContext, ArgumentType<?>> argumentType) {
        return Registry.register(DeclarativeUIBuiltInRegistries.COMMAND_ARGUMENT_PROVIDER, ResourceLocation.parse(string), new CommandArgumentProvider(Either.right(argumentType)));
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.COMMAND_ARGUMENT_PROVIDER.byNameCodec();
        register("string/word", StringArgumentType::word);
        register("string/string", StringArgumentType::string);
        register("string/greedy", StringArgumentType::greedyString);

        register("entity/entity", EntityArgument::entity);
        register("entity/entities", EntityArgument::entities);
        register("entity/player", EntityArgument::player);
        register("entity/players", EntityArgument::players);

        register("team", TeamArgument::team);
        register("gamemode", GameModeArgument::gameMode);
        register("nbt", CompoundTagArgument::compoundTag);
        register("uuid", UuidArgument::uuid);
        register("identifier", ResourceLocationArgument::id);
        register("text", ComponentArgument::textComponent);
        register("function", FunctionArgument::functions);
        register("item", ItemArgument::new);

        register("scoreboard/objective", ObjectiveArgument::objective);
        register("scoreboard/holders", ScoreHolderArgument::scoreHolders);

        for (Registry<?> registry : BuiltInRegistries.REGISTRY) {
            var location = registry.key().location();
            String registryName = location.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE) ? location.getPath() : location.getNamespace() + "/" + location.getPath();
            register("registry/" + registryName, () -> ResourceKeyArgument.key(registry.key()));
        }
    }
}
