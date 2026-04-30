package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record ScoreHolderArgument(boolean multiple) implements CommandArgument<net.minecraft.commands.arguments.ScoreHolderArgument.Result> {
    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return CommandArgumentTypes.SCORE_HOLDER_ARGUMENT.argumentTypeInfo();
    }

    @Override
    public ArgumentType<net.minecraft.commands.arguments.ScoreHolderArgument.Result> getArgumentType(CommandBuildContext buildContext) {
        return new net.minecraft.commands.arguments.ScoreHolderArgument(this.multiple);
    }
}
