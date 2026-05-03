package eu.cj4.declarativeui.mixin.argument;

import net.minecraft.commands.arguments.EntityArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityArgument.class)
public interface EntityArgumentAccessor {
    @Invoker("<init>")
    static EntityArgument create(boolean single, boolean playersOnly) {
        throw new UnsupportedOperationException();
    }
}
