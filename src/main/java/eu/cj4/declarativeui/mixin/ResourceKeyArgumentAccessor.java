package eu.cj4.declarativeui.mixin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ResourceKeyArgument.class)
public interface ResourceKeyArgumentAccessor {
    @Invoker
    static <T> Holder.Reference<T> callResolveKey(CommandContext<CommandSourceStack> commandContext, String string, ResourceKey<Registry<T>> resourceKey, DynamicCommandExceptionType dynamicCommandExceptionType) {
        throw new UnsupportedOperationException();
    }
}
