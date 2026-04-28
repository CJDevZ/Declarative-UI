package eu.cj4.declarativeui.mixin.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StringArgumentType.class)
public interface StringArgumentTypeAccessor {
    @Invoker("<init>")
    static StringArgumentType create(StringArgumentType.StringType type) {
        throw new UnsupportedOperationException();
    }
}
