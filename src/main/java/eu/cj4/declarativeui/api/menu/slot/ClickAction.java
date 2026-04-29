package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ClickAction implements StringRepresentable {
    CLOSE("close"),
    REFRESH("refresh"),
    REFRESH_TITLE("refresh_title"),
    NONE("none");

    public static final Codec<ClickAction> CODEC = StringRepresentable.fromEnum(ClickAction::values);

    private final String name;

    ClickAction(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
