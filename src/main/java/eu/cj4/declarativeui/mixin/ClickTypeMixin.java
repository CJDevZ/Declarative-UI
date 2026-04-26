package eu.cj4.declarativeui.mixin;

import eu.pb4.sgui.api.ClickType;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Locale;

@Mixin(ClickType.class)
public abstract class ClickTypeMixin implements StringRepresentable {

    @Override
    public @NotNull String getSerializedName() {
        return ((Enum<?>)(Object) this).name().toLowerCase(Locale.ROOT);
    }
}
