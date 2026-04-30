package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import eu.cj4.declarativeui.mixin.argument.EntityArgumentAccessor;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record EntityArgument(boolean single, boolean playersOnly) implements CommandArgument<EntitySelector> {
    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return CommandArgumentTypes.ENTITY_ARGUMENT.argumentTypeInfo();
    }

    @Override
    public ArgumentType<EntitySelector> getArgumentType(CommandBuildContext buildContext) {
        return EntityArgumentAccessor.create(this.single, this.playersOnly);
    }
}
