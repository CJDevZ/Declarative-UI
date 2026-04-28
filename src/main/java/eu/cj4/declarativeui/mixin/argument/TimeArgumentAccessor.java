package eu.cj4.declarativeui.mixin.argument;

import net.minecraft.commands.arguments.TimeArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TimeArgument.class)
public interface TimeArgumentAccessor {
    @Accessor
    int getMinimum();
}
