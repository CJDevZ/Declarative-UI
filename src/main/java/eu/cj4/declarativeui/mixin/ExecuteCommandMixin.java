package eu.cj4.declarativeui.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import eu.cj4.declarativeui.impl.container.DeclaredContainer;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.server.commands.ExecuteCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

@Mixin(ExecuteCommand.class)
public class ExecuteCommandMixin {
    @Shadow
    private static Collection<CommandSourceStack> expect(CommandContext<CommandSourceStack> commandContext, boolean bl, boolean bl2) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static Command<CommandSourceStack> createNumericConditionalHandler(boolean bl, ExecuteCommand.CommandNumericPredicate commandNumericPredicate) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @WrapOperation(method = "addConditionals", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
    private static ArgumentBuilder<CommandSourceStack, ?> addContainerCondition(LiteralArgumentBuilder<CommandSourceStack> instance, ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, Operation<ArgumentBuilder<CommandSourceStack, ?>> original, @Local(argsOnly = true) CommandNode<CommandSourceStack> commandNode, @Local(argsOnly = true) CommandBuildContext commandBuildContext, @Local(argsOnly = true) boolean bl) {
        if (!instance.getLiteral().equals("items")) {
            original.call(instance, argumentBuilder);
        }
        return original.call(instance, argumentBuilder)
                .then(Commands.literal("container")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("container", ResourceKeyArgument.key(DeclarativeUIRegistries.CONTAINER))
                                        .then(Commands.argument("range", RangeArgument.intRange())
                                                .then(Commands.argument("item_predicate", ItemPredicateArgument.itemPredicate(commandBuildContext))
                                                        .fork(commandNode, context -> expect(context, bl, DeclaredContainer.countItems(context) > 0))
                                                        .executes(createNumericConditionalHandler(bl, DeclaredContainer::countItems))
                                                )
                                        )
                                )
                        )
                );
    }
}
