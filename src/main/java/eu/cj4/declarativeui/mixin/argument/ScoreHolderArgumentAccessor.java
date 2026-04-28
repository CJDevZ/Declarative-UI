package eu.cj4.declarativeui.mixin.argument;

import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.TimeArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScoreHolderArgument.class)
public interface ScoreHolderArgumentAccessor {
    @Accessor
    boolean isMultiple();
}
