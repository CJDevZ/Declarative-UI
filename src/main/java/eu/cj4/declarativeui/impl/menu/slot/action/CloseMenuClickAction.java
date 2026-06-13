package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.Nullable;

public record CloseMenuClickAction() implements ClickAction {
    public static final CloseMenuClickAction INSTANCE = new CloseMenuClickAction();
    public static final MapCodec<CloseMenuClickAction> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<CloseMenuClickAction> codec() {
        return MAP_CODEC;
    }

    @Override
    public void click(Menu declaredMenu, SlotBasedGui gui, @Nullable CompoundTag compoundTag) {
        gui.close();
    }
}
